package io.shortcut.redux

import tw.geothings.rekotlin.Action

abstract class Request : Action {

    abstract suspend fun execute()
}
