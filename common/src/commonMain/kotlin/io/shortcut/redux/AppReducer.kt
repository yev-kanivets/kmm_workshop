package io.shortcut.redux

import io.shortcut.features.space.redux.spaceReducer
import tw.geothings.rekotlin.Action

fun appReducer(action: Action, state: AppState?): AppState {
    requireNotNull(state)
    return AppState(
            space = spaceReducer(action, state)
    )
}
