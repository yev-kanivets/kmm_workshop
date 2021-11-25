package io.shortcut.redux

import io.shortcut.features.space.redux.SpaceState
import tw.geothings.rekotlin.StateType

data class AppState(
        val space: SpaceState = SpaceState()
) : StateType
