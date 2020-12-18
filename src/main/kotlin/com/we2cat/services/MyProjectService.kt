package com.we2cat.services

import com.intellij.openapi.project.Project
import com.we2cat.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
