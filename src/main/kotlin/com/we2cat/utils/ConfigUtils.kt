package com.we2cat.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.we2cat.entity.GenMpcConfig
import com.we2cat.entity.JsonConfig
import java.io.File
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
    get():String? = field ?: "${getUserDir()}/.we2cat/".also { field = it }

/**
 * 当前操作系统
 */
private var os: String? = null
    get():String? = field ?: System.getProperty("os.name").also { field = it }

/**
 * 生成Mybatis-plus代码本地配置文件名
 */
private const val genMpcConf = "gen_mpc"

private const val jsonConf = "json"

/**
 * 获取用户we2cat目录
 */

/**
 * 获取生成Mybatis-plus代码本地配置
 */
fun getGenMpcLocalConfig(): GenMpcConfig? {
    return getContent(genMpcConf, GenMpcConfig::class.java)
}

/**
 * 获取本地json配置
 */
fun getJsonConf(): JsonConfig {
    return getContent(jsonConf, JsonConfig::class.java)
        ?: return JsonConfig().also { saveContent(jsonConf, it) }
}

/**
 * 保存生成Mybatis-plus代码本地配置
 */
fun saveGenMpcLocalConfig(gmpConfig: GenMpcConfig) {
    saveContent(genMpcConf, gmpConfig)
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
        ).use { bf ->
            runCatching {
                val sb = StringBuilder()
                var line: String?
                while (bf.readLine().also { line = it } != null) {
                    sb.append(line)
                }
                return Gson().fromJson(sb.toString(), clazz)
            }
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

/**
 * 获取系统用户目录
 */
fun getUserDir(): String = System.getProperty("user.home")

/**
 * 当前系统是否是windows
 */
fun isWindows(): Boolean = os!!.toLowerCase().indexOf("windows") >= 0