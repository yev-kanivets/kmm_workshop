package io.shortcut.redux.middlewares

import io.shortcut.redux.ToastAction
import tw.geothings.rekotlin.Middleware
import tw.geothings.rekotlin.StateType

interface MessageHandler {

    fun handle(message: String?)
}

val messageHandlers = mutableListOf<MessageHandler>()

val messageMiddleware: Middleware<StateType> = { _, _ ->
    { next ->
        { action ->
            if (action is ToastAction) {
                messageHandlers.forEach { it.handle(action.message) }
            }
            next(action)
        }
    }
}
