package com.we2cat.utils

/**
 * 获取值 如果前者不为空则返回else
 */
fun <T> getOrElse(t: T?, elseT: T): T {
    return if (t is String) if (t.isBlank()) elseT else t else t ?: elseT
}