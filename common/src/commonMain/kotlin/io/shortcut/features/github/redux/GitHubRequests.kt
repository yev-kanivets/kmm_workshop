package io.shortcut.features.github.redux

import io.shortcut.features.github.entity.Issue
import io.shortcut.features.github.repository.GitHubRepository
import io.shortcut.network.Response
import io.shortcut.redux.Request
import io.shortcut.redux.ToastAction
import io.shortcut.redux.store
import tw.geothings.rekotlin.Action

class GitHubRequests {

    class FetchIssues: Request() {

        private val gitHubRepository = GitHubRepository()

        override suspend fun execute() {
            val result = when(val response = gitHubRepository.getIssues()) {
                is Response.Success -> Success(response.result)
                is Response.Failure -> Failure(response.error)
            }
            store.dispatch(result)
        }

        data class Success(val issues: List<Issue>): Action
        data class Failure(override val message: String?): ToastAction
    }
}