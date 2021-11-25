package io.shortcut.android

import android.app.Application
import io.shortcut.android.components.AndroidMessageHandler
import io.shortcut.redux.middlewares.messageHandlers
import io.shortcut.util.Strings

class KMMApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        app = this

        initMessageHandler()
        initCommonStrings()
    }

    private fun initMessageHandler() {
        messageHandlers.add(AndroidMessageHandler())
    }

    private fun initCommonStrings() {
        Strings.context = this
    }

    companion object {

        lateinit var app: KMMApplication
    }
}
