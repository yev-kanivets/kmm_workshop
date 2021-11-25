package io.shortcut.redux.middlewares

import io.shortcut.redux.Request
import io.shortcut.redux.SyncRequest
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import tw.geothings.rekotlin.Middleware
import tw.geothings.rekotlin.StateType

private val scope = MainScope()

val appMiddleware: Middleware<StateType> = { _, _ ->
    { next ->
        { action ->
            if (action is Request) executeRequest(action)
            if (action is SyncRequest) executeSyncRequest(action)

            next(action)
        }
    }
}

private fun executeRequest(action: Request) = scope.launch { action.execute() }

private fun executeSyncRequest(action: SyncRequest) = scope.launch { action.execute() }
