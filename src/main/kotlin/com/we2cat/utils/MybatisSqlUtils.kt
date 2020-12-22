package com.we2cat.utils

import com.intellij.ui.JBColor
import com.we2cat.panel.logPrintln

/**
 * Created by hel on 2020/12/18 14:42
 */
private const val Preparing = "Preparing: "
private const val Parameters = "Parameters:"
private const val StartWith = " "
private val paramRegex = Regex(" .+\\([A-Za-z]{1,10}\\),?")
private val paramTypeRegex = Regex("\\)[\\n]?")

private var preparingLine = ""

private var parametersLine = ""

/**
 * 进行日志解析并打印
 * [projectBasePath] 项目路径
 * [line] 当前行内容
 */
fun mybatisSqlLog(projectBasePath: String, line: String) {
    if (!line.startsWith(StartWith)) {
        setLine(line)
        if (preparingLine.isNotBlank() && parametersLine.isNotEmpty()) {
            jointSql(preparingLine, parametersLine)?.let {
                logPrintln(
                    projectBasePath,
                    "$StartWith${it}",
                    getColor(preparingLine[0].toLowerCase())
                )
            }
            resetLine()
        }
    }
}

/**
 * 参数拼接
 */
private fun jointSql(preparing: String, parameters: String): String? {
    var placeArray: Array<String?>? = null
    val paramArray = if (parameters.contains(paramRegex))
        parameters.split(",")
            .also {
                placeArray = arrayOfNulls(it.size)
            }
            .mapIndexed { index, s ->
                val paramList = s.split("(")
                placeArray!![index] = getPlace(paramList[1].replace(paramTypeRegex, ""))
                paramList[0].substring(1)
            }
            .toTypedArray()
    else emptyArray()
    if (preparing.filter { c -> c == '?' }.count() != paramArray.size) return null
    if (paramArray.isEmpty()) return preparing
    var formatSql = preparing
    placeArray?.forEach { formatSql = formatSql.replaceFirst("?", it!!) }
    return formatSql.format(*paramArray)
}

/**
 * 获取占位符
 */
private fun getPlace(str: String): String = when (str) {
    "Integer", "Long", "Float", "Double" -> "%s"
    else -> "'%s'"
}

/**
 * 设置行数据
 */
private fun setLine(line: String) {
    if (line.contains(Preparing)) preparingLine = line.split(Preparing)[1]
    if (line.contains(Parameters)) parametersLine = line.split(Parameters)[1]
}

/**
 * 重置行
 */
private fun resetLine() {
    preparingLine = ""
    parametersLine = ""
}

/**
 * 获取颜色
 */
private fun getColor(sqlHead: Char) = when (sqlHead) {
    'i' -> JBColor.GREEN
    'd' -> JBColor.RED
    'u' -> JBColor.BLUE
    's' -> JBColor.ORANGE
    else -> JBColor.GRAY
}
