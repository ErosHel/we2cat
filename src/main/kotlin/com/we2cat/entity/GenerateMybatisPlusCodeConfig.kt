package com.we2cat.entity

/**
 * Created by hel on 2020/12/31 13:30
 */
class GenerateMybatisPlusCodeConfig {

    /**
     * 数据库用户
     */
    var dbUser: String? = null

    /**
     * 数据库密码
     */
    var dbPw: String? = null

    /**
     * 数据库连接
     */
    var dbUrl: String? = null

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
     * 数据库名称
     */
    var dbName: String? = null

    /**
     * 表名 逗号分隔
     */
    var table: String? = null

    /**
     * 输出目录
     */
    var outPath: String? = null

}