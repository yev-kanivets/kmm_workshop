package io.shortcut.features.github.redux

import io.shortcut.features.github.entity.Issue

data class GitHubState(
    val issues: List<Issue> = emptyList(),
    val status: Status = Status.IDLE
) {

    enum class Status {
        IDLE, PENDING
    }
}
