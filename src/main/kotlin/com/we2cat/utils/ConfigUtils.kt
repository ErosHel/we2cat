package com.we2cat.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.we2cat.entity.GenerateMybatisPlusCodeConfig
import java.io.*
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path

/**
 * Created by hel on 2020/12/31 13:40
 */
/**
 * we2cat目录
 */
private var userHome: String? = null
    get():String? = field ?: "${System.getProperty("user.home")}/.we2cat/".also { field = it }

/**
 * 生成Mybatis-plus代码本地配置文件名
 */
private const val genMpcName = "gen_mpc"

/**
 * 获取用户we2cat目录
 */

/**
 * 获取生成Mybatis-plus代码本地配置
 */
fun getGenMpcLocalConfig(): GenerateMybatisPlusCodeConfig? {
    return getContent(genMpcName, GenerateMybatisPlusCodeConfig::class.java)
}

/**
 * 保存生成Mybatis-plus代码本地配置
 */
fun saveGenMpcLocalConfig(gmpConfig: GenerateMybatisPlusCodeConfig) {
    saveContent(genMpcName, gmpConfig)
}

/**
 * 保存内容到本地
 * 需注意先必须调用获取本地内容之后再做进行保存
 */
private fun saveContent(fileName: String, entity: Any) {
    Files.newBufferedWriter(
        Path.of("${userHome}$fileName.conf"),
        Charset.forName("UTF-8")
    ).use { it.write(GsonBuilder().setPrettyPrinting().create().toJson(entity)) }
}

/**
 * 获取本地内容
 */
private fun <T> getContent(fileName: String, clazz: Class<T>): T? {
    val file = File("${userHome}$fileName.conf")
    if (file.exists()) {
        Files.newBufferedReader(
            Path.of("${userHome}$fileName.conf"),
            Charset.forName("UTF-8")
        ).use {
            val sb = StringBuilder()
            var line: String?
            while (it.readLine().also { line = it } != null) {
                sb.append(line)
            }
            return Gson().fromJson(sb.toString(), clazz)
        }
    } else {
        val dir = File(userHome!!)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        file.createNewFile()
    }
    return null
}