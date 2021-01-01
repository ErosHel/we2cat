package com.we2cat.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.we2cat.utils.parseAssignByUppercase
import com.we2cat.utils.parseUppercaseByAssign

/**
 * Created by hel on 2020/12/21 14:22
 */
class StringForSelectionAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        WriteCommandAction.runWriteCommandAction(e.project) {
            val editor = e.getData(CommonDataKeys.EDITOR)!!
            val selectedText = editor.caretModel.currentCaret.selectedText
            if (selectedText.isNullOrBlank()) {
                return@runWriteCommandAction
            }
            val replaceText = when {
                selectedText.contains(" ") -> selectedText.replace(" ", "_")
                selectedText.contains("_") -> parseAssignByUppercase(selectedText, '_')
                else -> parseUppercaseByAssign(selectedText, '_')
            }
            val selectionModel = editor.selectionModel
            editor.document.replaceString(
                selectionModel.selectionStart,
                selectionModel.selectionEnd, replaceText
            )
        }
    }

    override fun update(e: AnActionEvent) {
        val editor = e.getData(CommonDataKeys.EDITOR)
        e.presentation.isEnabled = e.getData(CommonDataKeys.PROJECT) != null
                && editor != null && editor.selectionModel.hasSelection()
    }

}