package com.we2cat.entity

import com.we2cat.utils.getOrElse

class GenMpcModelConfig {

    /**
     * 包名
     */
    var pagName: String? = null

    /**
     * 模块名称 逗号分隔
     */
    var modelName: String? = null

    /**
     * 子模块
     */
    var childModelName: String? = null

    /**
     * 领域包名
     */
    var domainName: String? = null

    /**
     * 持久层包名
     */
    var mapperPagName: String = ".mapper"
        set(value) {
            field = getOrElse(value, field)
        }

    /**
     * 持久层后缀
     */
    var mapperEnd: String = "Mapper"
        set(value) {
            field = getOrElse(value, field)
        }

    /**
     * 领域层包名
     */
    var domainPagName: String = ".entity"
        set(value) {
            field = getOrElse(value, field)
        }

    /**
     * 领域层后缀
     */
    var domainEnd: String = ""
        set(value) {
            field = getOrElse(value, field)
        }

    /**
     * 逻辑删除字段
     */
    var deleteField: String = "deleted"
        set(value) {
            field = getOrElse(value, field)
        }

}