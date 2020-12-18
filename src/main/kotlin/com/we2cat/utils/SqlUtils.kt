package com.we2cat.utils

/**
 * Created by hel on 2020/12/18 14:42
 */
const val Sql_StartWith = "  "
const val Sql_Preparing = "Preparing:"
const val Sql_Parameters = "Parameters:"
private val ParamRegex = Regex(" .+\\([A-Za-z]{1,10}\\),?")

/**
 * 解析sql
 */
fun parseSql(preparing: String): String = preparing.replace("?", "%s")

/**
 * 解析参数
 */
fun parseParam(parameters: String): List<String> {
    if (parameters.matches(ParamRegex)) {
        val map = parameters.split(",").map { it.split("(")[0] }
        return map
    }
    return emptyList()
}

/**
 * 参数拼接
 */
fun sqlJoint(preparing: String, parameters: String): String {
    val sql = parseSql(preparing)
    val args = parseParam(parameters)
    return "$Sql_StartWith${if (args.isEmpty()) sql else String.format(sql, args)}"
}