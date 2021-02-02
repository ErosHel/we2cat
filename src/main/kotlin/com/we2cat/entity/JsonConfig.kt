package com.we2cat.entity

import com.we2cat.utils.getOrElse

/**
 * Created by hel on 2021/2/2 15:05
 */
class JsonConfig {

    /**
     * 生成json的层数
     */
    var level: Int = 4
        set(value) {
            field = getOrElse(value, field)
        }

    /**
     * 生成json包白名单
     */
    var whitelist: ArrayList<String> = arrayListOf()
        set(value) {
            field = getOrElse(value, field)
        }

    /**
     * 生成json包黑名单
     */
    var blacklist: ArrayList<String> = arrayListOf("java.*", "sun.*")
        set(value) {
            field = getOrElse(value, field)
        }

}