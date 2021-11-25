package io.shortcut.redux

import io.shortcut.redux.middlewares.messageMiddleware
import io.shortcut.redux.middlewares.appMiddleware
import tw.geothings.rekotlin.Store

val store by lazy {
    Store(
            reducer = ::appReducer,
            state = AppState(),
            middleware = listOf(appMiddleware, messageMiddleware)
    )
}
