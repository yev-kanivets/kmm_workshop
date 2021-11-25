package io.shortcut.features.github.redux

import io.shortcut.redux.AppState
import tw.geothings.rekotlin.Action

fun getHubReducer(action: Action, state: AppState): GitHubState {
    var newState = state.gitHub

    when(action) {
        is GitHubRequests.FetchIssues -> {
            newState = newState.copy(status = GitHubState.Status.PENDING)
        }
        is GitHubRequests.FetchIssues.Success -> {
            newState = newState.copy(
                issues = action.issues,
                status = GitHubState.Status.IDLE
            )
        }
        is GitHubRequests.FetchIssues.Failure -> {
            newState = newState.copy(status = GitHubState.Status.IDLE)
        }
    }

    return newState
}