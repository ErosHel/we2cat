package com.we2cat.entity

/**
 * Created by hel on 2020/12/31 13:30
 */
class GenMpcConfig {

    /**
     * 数据库相关配置
     */
    var db: GenMpcDbConfig? = null

    /**
     * 模块相关配置
     */
    var model: GenMpcModelConfig? = null

    /**
     * 其他配置
     */
    var other: GenMpcOtherConfig? = null

}