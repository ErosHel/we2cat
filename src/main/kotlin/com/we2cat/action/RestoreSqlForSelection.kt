package com.we2cat.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.we2cat.icons.Icon_Mybatis

/**
 * Created by hel on 2020/12/16 18:08
 */
class RestoreSqlForSelection : AnAction(Icon_Mybatis) {

    override fun actionPerformed(e: AnActionEvent) {
        val selectedText = e.getData(LangDataKeys.EDITOR)!!
            .caretModel.currentCaret.selectedText
        println(selectedText)
    }

    override fun update(e: AnActionEvent) {
        templatePresentation.isEnabled = true
    }

}