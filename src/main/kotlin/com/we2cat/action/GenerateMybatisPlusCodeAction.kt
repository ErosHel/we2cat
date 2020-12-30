package com.we2cat.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.we2cat.gui.GenerateMybatisPlusCode

/**
 * Created by hel on 2020/12/30 10:14
 */
class GenerateMybatisPlusCodeAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        GenerateMybatisPlusCode(e.project)
    }

}