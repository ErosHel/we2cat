package com.we2cat.entity

import com.we2cat.utils.getOrElse

class GenMpcOtherConfig {

    /**
     * 生成作者
     */
    var author: String? = null

    /**
     * 输出目录
     */
    var outPath: String? = null

    /**
     * 是否覆盖已存在文件
     */
    var fileOverride: Boolean = true
        set(value) {
            field = getOrElse(value, field)
        }

}