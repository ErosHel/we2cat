package com.we2cat.utils

/**
 * Created by hel on 2020/12/21 14:46
 */
/**
 * 将大写字母根据指定分隔符转换
 */
fun parseUppercaseByAssign(text: String, assign: Char): String =
    StringBuilder().let { sb ->
        text.forEachIndexed { index, c ->
            when {
                index == 0 -> {
                    sb.append(c.toLowerCase())
                }
                c.toInt() in 65..90 -> sb.append("${assign}${c + 32}")
                else -> sb.append(c)
            }
        }
        sb.toString()
    }

/**
 * 根据指定分隔符转换为大写
 */
fun parseAssignByUppercase(text: String, assign: Char): String =
    StringBuilder().let { sb ->
        var i = 0
        while (i < text.length) {
            if (text[i].toInt() in 65..90)
                return@let text.toLowerCase()
            if (text[i] == assign) {
                val c = text[++i]
                if (c.toInt() in 97..122)
                    sb.append(c - 32)
                else sb.append(c)
            } else sb.append(text[i])
            i++
        }
        sb.toString()
    }