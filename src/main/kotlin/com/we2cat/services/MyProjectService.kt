package com.we2cat.services

import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.project.Project
import com.we2cat.MyBundle
import com.we2cat.listeners.EditorListener

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
        project.messageBus.connect().subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, EditorListener(project))
    }

}
