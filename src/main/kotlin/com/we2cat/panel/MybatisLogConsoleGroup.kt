package com.we2cat.panel

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAware

/**
 * Created by hel on 2020/12/17 14:04
 */
sealed class MybatisLogConsoleGroup {

    class FilterAction(private val filterRun: () -> Unit) :
        AnAction("过滤", "Filter", AllIcons.General.Filter), DumbAware {

        override fun actionPerformed(e: AnActionEvent) {
            filterRun.invoke()
        }

    }

}
