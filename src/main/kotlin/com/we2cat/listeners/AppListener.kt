package com.we2cat.listeners

import com.intellij.ide.AppLifecycleListener

/**
 * Created by hel on 2020/12/21 17:46
 */
class AppListener : AppLifecycleListener {

    override fun appFrameCreated(commandLineArgs: MutableList<String>) {
        println("EditorListener.appFrameCreated")
    }

    override fun welcomeScreenDisplayed() {
        println("EditorListener.welcomeScreenDisplayed")
    }

    override fun projectFrameClosed() {
        println("EditorListener.projectFrameClosed")
    }

    override fun projectOpenFailed() {
        println("EditorListener.projectOpenFailed")
    }

    override fun appClosing() {
        println("EditorListener.appClosing")
    }

    override fun appWillBeClosed(isRestart: Boolean) {
        println("EditorListener.appWillBeClosed")
    }

}