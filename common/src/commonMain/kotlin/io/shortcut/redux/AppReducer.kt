package io.shortcut.redux

import io.shortcut.features.github.redux.getHubReducer
import tw.geothings.rekotlin.Action

fun appReducer(action: Action, state: AppState?): AppState {
    requireNotNull(state)
    return AppState(
            gitHub = getHubReducer(action, state)
    )
}
