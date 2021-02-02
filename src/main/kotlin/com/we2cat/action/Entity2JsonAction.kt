package com.we2cat.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.we2cat.entity.JsonConfig
import com.we2cat.gui.Entity2JsonGui
import com.we2cat.utils.getJsonConf

/**
 * Created by hel on 2021/2/1 17:00
 */
class Entity2JsonAction : AnAction() {

    private var project: Project? = null

    override fun actionPerformed(e: AnActionEvent) {
        entity2JsonGui?.run {
            val editorText = e.getData(CommonDataKeys.EDITOR)!!.document.text
            val className = classRegex.find(editorText)?.value?.replace(classNameRegex, "")
            if (!className.isNullOrBlank()) {
                isVisible = true
                jsonConfig = getJsonConf()
                project = e.project!!
                setJson(className, genJson(editorText, 1, false))
            }
        }

    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = e.getData(CommonDataKeys.EDITOR) != null
    }

    private var entity2JsonGui: Entity2JsonGui? = null
        get() = if (field == null) {
            field = Entity2JsonGui();field
        } else field

    private var jsonConfig: JsonConfig? = null

    private val classRegex = Regex("class [a-zA-z0-9_]+ ")

    private val classNameRegex = Regex("class| ")

    private val filedRegex = Regex("\\s.+ [a-zA-z][a-zA-z0-9_]+;")

    private val filedMultipleRegex = Regex("(<[a-zA-z0-9_]+>)|([a-zA-z0-9_]+\\[])")

    private val filedMultipleNameRegex = Regex("[<>]|\\[]")

    private val pagRegexString = "package "

    /**
     * 生成Json
     */
    private fun genJson(editorText: String, blank: Int, isMultiple: Boolean): String {
        val filedList = filedRegex.findAll(editorText).toList()
        //判断是否层数限制或字段是否为空
        if (blank >= jsonConfig!!.level + (if (isMultiple) 2 else 1) || filedList.isEmpty()) {
            return "\"\""
        }
        val sb = StringBuilder("{\n")
        filedList.forEachIndexed { index, field ->
            val split = field.value.split(Regex("\\s"))
            val blankStr = String(CharArray(blank * 2) { ' ' })
            val filed = split[split.size - 1].replace(";", "")
            val type = split[split.size - 2]
            //字段前半
            sb.append("$blankStr\"$filed\": ")
            //后半数据
            sb.append(
                when {
                    type.contains(Regex("List|\\[]|Set")) ->
                        "[\n$blankStr  ${getClassJson(getMultipleTypeName(type), blank + 2, true)}\n$blankStr]"
                    type.matches(Regex("Integer|Long")) -> -1
                    type.matches(Regex("Float|Double|BigInteger|BigDecimal")) -> 0.00
                    else -> getClassJson(type, blank + 1, false)
                }
            )
            if (filedList.size - 1 != index) {
                sb.append(",\n")
            }
        }
        return sb.append("\n${String(CharArray((blank - 1) * 2) { ' ' })}}").toString()
    }

    /**
     * 获取集合数组的实体类名
     */
    private fun getMultipleTypeName(multipleFiled: String): String {
        return filedMultipleRegex.find(multipleFiled)!!.value.replace(filedMultipleNameRegex, "")
    }

    /**
     * 获取类并生成json
     */
    private fun getClassJson(className: String, blank: Int, isMultiple: Boolean): String {
        val files = getJavaFiles(className)
        if (files.isEmpty() || !isFilter(files[0].text)) {
            return "\"\""
        }
        return genJson(files[0].text, blank, isMultiple)
    }

    /**
     * 是否满足白名单或黑名单
     */
    private fun isFilter(text: String): Boolean {
        jsonConfig!!.whitelist.forEach { if (isContains(text, it)) return true }
        jsonConfig!!.blacklist.forEach { if (isContains(text, it)) return false }
        return true
    }

    /**
     * 是否包含这个成员
     */
    private fun isContains(text: String, member: String) =
        text.contains(Regex("$pagRegexString${member.replace("*", ".+")}"))

    /**
     * 获取java文件
     */
    private fun getJavaFiles(className: String): Array<out PsiFile> {
        return FilenameIndex.getFilesByName(
            project!!, "${className}.java", GlobalSearchScope.allScope(project!!)
        )
    }

}