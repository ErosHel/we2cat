package com.we2cat.listeners

import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.project.Project

/**
 * Created by hel on 2020/12/21 18:28
 */
class EditorListener(project: Project) : FileEditorManagerListener {

    override fun selectionChanged(event: FileEditorManagerEvent) {
        println("selectionChanged${event.newEditor!!.name}")
    }

}