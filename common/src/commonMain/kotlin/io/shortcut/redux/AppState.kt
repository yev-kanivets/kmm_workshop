package io.shortcut.redux

import io.shortcut.features.github.redux.GitHubState
import tw.geothings.rekotlin.StateType

data class AppState(
        val gitHub: GitHubState = GitHubState()
) : StateType
