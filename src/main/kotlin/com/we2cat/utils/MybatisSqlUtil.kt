package com.we2cat.utils

import com.we2cat.panel.logPrintln
import java.awt.Color

/**
 * Created by hel on 2020/12/18 14:42
 */
private const val Preparing = "Preparing: "
private const val Parameters = "Parameters:"
private const val StartWith = " "
private val paramRegex = Regex(" .+\\([A-Za-z]{1,10}\\),?")

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
            logPrintln(
                projectBasePath,
                jointSql(preparingLine, parametersLine),
                getColor(preparingLine[0].toLowerCase())
            )
            resetLine()
        }
    }
}

/**
 * 参数拼接
 */
private fun jointSql(preparing: String, parameters: String): String =
    parseParam(parameters).let {
        "$StartWith${if (it.isEmpty()) preparing else parseSql(preparing).format(*it)}"
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
    'i' -> Color.GREEN
    'd' -> Color.RED
    'u' -> Color.BLUE
    's' -> Color.ORANGE
    else -> Color.GRAY
}

/**
 * 解析sql
 * [preparing] sql本身
 */
private fun parseSql(preparing: String): String =
    preparing.replace("?", "'%s'")

/**
 * 解析参数
 */
private fun parseParam(parameters: String): Array<String> =
    if (parameters.contains(paramRegex)) parameters.split(",")
        .map { it.split("(")[0].substring(1) }
        .toTypedArray()
    else emptyArray()
