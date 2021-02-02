package com.we2cat.listeners

import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.GlobalSearchScopeUtil
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Created by hel on 2020/12/21 18:28
 */
class EditorListener(val project: Project) : FileEditorManagerListener {

    override fun selectionChanged(event: FileEditorManagerEvent) {
        val filesByName = FilenameIndex.getFilesByName(project, "AbcEntity", GlobalSearchScope.allScope(project))
        println(filesByName.size)
//        val inputStream = newEditor.file!!.inputStream
//        val inputStreamReader = InputStreamReader(inputStream)
//        inputStreamReader.readLines()
//            .filter { it.isNotBlank() }
//            .forEach { println(it) }
    }

}