package io.shortcut.redux

import tw.geothings.rekotlin.Action

interface ToastAction : Action {

    val message: String?
}
