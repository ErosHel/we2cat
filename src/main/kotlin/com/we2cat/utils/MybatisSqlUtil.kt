package com.we2cat.utils

import com.we2cat.panel.logPrintln
import java.awt.Color

/**
 * Created by hel on 2020/12/18 14:42
 */
const val Sql_Preparing = "Preparing: "
const val Sql_Parameters = "Parameters:"
private val ParamRegex = Regex(" .+\\([A-Za-z]{1,10}\\),?")
private const val startWith = " "

private var preparingLine = ""

private var parametersLine = ""

/**
 * 进行日志解析并打印
 * [projectBasePath] 项目路径
 * [line] 当前行内容
 */
fun mybatisSqlLog(projectBasePath: String, line: String) {
    if (!line.startsWith(startWith)) {
        setLine(line)
        if (preparingLine.isNotBlank() && parametersLine.isNotEmpty()) {
            logPrintln(projectBasePath, jointSql(preparingLine, parametersLine), getColor())
            resetLine()
        }
    }
}

/**
 * 设置行数据
 */
private fun setLine(line: String) {
    if (line.contains(Sql_Preparing)) preparingLine = line.split(Sql_Preparing)[1]
    if (line.contains(Sql_Parameters)) parametersLine = line.split(Sql_Parameters)[1]
}

/**
 * 获取颜色
 */
private fun getColor() = when {
    preparingLine.startsWith("insert") -> Color.GREEN
    preparingLine.startsWith("delete") -> Color.RED
    preparingLine.startsWith("update") -> Color.BLUE
    preparingLine.startsWith("select") -> Color.ORANGE
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
    if (parameters.contains(ParamRegex)) parameters.split(",")
        .map { it.split("(")[0].substring(1) }
        .toTypedArray()
    else emptyArray()

/**
 * 参数拼接
 */
private fun jointSql(preparing: String, parameters: String): String =
    parseParam(parameters).let {
        "$startWith${if (it.isEmpty()) preparing else parseSql(preparing).format(*it)}"
    }

/**
 * 重置行
 */
private fun resetLine() {
    preparingLine = ""
    parametersLine = ""
}