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
private val typeRegex = Regex("[, ]|\\s")
private val paramAndTypeRegex = Regex("\\(.*?\\)[,\\s]?")

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
                    "$StartWith$it",
                    getColor(preparingLine[0].lowercaseChar())
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
    val paramArray = if (parameters.contains(paramRegex))
        getParams(parameters) else emptyArray()
    if (preparing.filter { c -> c == '?' }.count() != paramArray.size) return null
    if (paramArray.isEmpty()) return preparing
    return preparing.replace("?", "%s").format(*paramArray)
}

/**
 * 获取所有参数
 */
fun getParams(parameters: String): Array<String> {
    val arrayList = arrayListOf<String>()
    val params = parameters.split(paramAndTypeRegex)
    paramAndTypeRegex.findAll(parameters).toList().forEachIndexed { index, s ->
        arrayList.add(
            getParamByType(
                params[index].substring(1),
                s.value.replace(typeRegex, "")
            )
        )
    }
    return arrayList.toTypedArray()
}

/**
 * 获取完整拼接参数
 */
fun getParamByType(param: String, type: String): String =
    when (type) {
        "(Integer)", "(Long)", "(Float)", "(Double)" -> param
        else -> "'$param'"
    }

/**
 * 设置行数据
 */
private fun setLine(line: String) {
    if (line.contains(Preparing)) {
        parametersLine = ""
        preparingLine = line.split(Preparing)[1]
    }
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
