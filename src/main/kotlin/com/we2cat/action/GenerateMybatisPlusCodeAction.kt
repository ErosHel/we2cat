package com.we2cat.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.we2cat.gui.GenerateMybatisPlusCodeGui

/**
 * Created by hel on 2020/12/30 10:14
 */
class GenerateMybatisPlusCodeAction : AnAction() {

    private var genDialog: GenerateMybatisPlusCodeGui? = null

    override fun actionPerformed(e: AnActionEvent) {
        getGenDialog(e.project!!).isVisible = true
    }

    private fun getGenDialog(project: Project): GenerateMybatisPlusCodeGui =
        genDialog ?: GenerateMybatisPlusCodeGui(project).also { genDialog = it }

}