package com.we2cat.panel

import com.intellij.execution.filters.Filter
import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.execution.ui.ConsoleView
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionToolbar
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.we2cat.action.gui.MybatisLogFilterSetting
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Font
import java.util.concurrent.ConcurrentHashMap
import javax.swing.JComponent
import javax.swing.JPanel

/**
 * Mybatis日志面板
 *
 * Created by hel on 2020/12/16 17:32
 */
class MybatisLogConsolePanel {

    /**
     * 创建log面板
     */
    fun createConsolePanel(project: Project): JComponent {
        val console = createConsole(project)
        val consolePanel = createConsolePanel(console)
        val toolbar = createActionToolbar(project, console)
        toolbar.setTargetComponent(consolePanel)

        val panel = SimpleToolWindowPanel(false, true)
        panel.setContent(consolePanel)
        panel.toolbar = toolbar.component

        val actionGroup = DefaultActionGroup()
        console.createConsoleActions().forEach { actionGroup.add(it) }
        return panel.component!!
    }

    /**
     * 创建输出面板
     */
    private fun createConsole(project: Project): ConsoleView =
        TextConsoleBuilderFactory.getInstance().createBuilder(project)
            .apply { filters(filterList) }
            .run { console }
            .apply { consoleMap[project.basePath!!] = this }

    /**
     * 创建JComponent
     */
    private fun createConsolePanel(consoleView: ConsoleView): JComponent =
        JPanel().apply {
            layout = BorderLayout()
            add(consoleView.component, BorderLayout.CENTER)
        }

    /**
     * 创建工具栏
     */
    private fun createActionToolbar(project: Project, consoleView: ConsoleView): ActionToolbar {
        val actionGroup = DefaultActionGroup()
        actionGroup.add(MybatisLogConsoleGroup.FilterAction {
            val setting = MybatisLogFilterSetting(project)
            setting.pack()
            setting.setSize(264, 157)
            setting.setLocationRelativeTo(null)
            setting.isVisible = true
        })
        actionGroup.add(consoleView.createConsoleActions()[2])
        actionGroup.add(consoleView.createConsoleActions()[3])
        actionGroup.add(consoleView.createConsoleActions()[5])
        return ActionManager.getInstance().createActionToolbar("", actionGroup, false)
    }

}

//多项目控制台独立性
private val consoleMap: ConcurrentHashMap<String, ConsoleView> = ConcurrentHashMap()

//过滤器
private val filterList: List<Filter> = ArrayList()

/**
 * 打印到控制台
 *
 * @param basePath 基础路径
 * @param line     行内容
 */
fun logPrintln(basePath: String, line: String) {
    logPrintln(basePath, line, null)
}

/**
 * 打印到控制台
 *
 * @param basePath 基础路径
 * @param line     行内容
 * @param color    颜色
 */
fun logPrintln(basePath: String, line: String, color: Color?) {
    consoleMap[basePath]?.run {
        val consoleViewContentType =
            color?.let {
                ConsoleViewContentType(
                    "",
                    TextAttributes(it, null, null, null, Font.PLAIN)
                )
            }
                ?: ConsoleViewContentType.USER_INPUT
        print(line, consoleViewContentType)
    }
}