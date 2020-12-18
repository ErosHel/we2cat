package com.we2cat.window

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import com.we2cat.icons.Icon_Mybatis
import com.we2cat.panel.MybatisLogConsolePanel

/**
 * Created by hel on 2020/12/17 14:26
 */
class MybatisLogToolWindow : ToolWindowFactory {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val consolePanel = MybatisLogConsolePanel().createConsolePanel(project)
        val factory = ContentFactory.SERVICE.getInstance()
        val content = factory.createContent(consolePanel, "", false)
        toolWindow.setIcon(Icon_Mybatis)
        toolWindow.contentManager.addContent(content)
    }

}