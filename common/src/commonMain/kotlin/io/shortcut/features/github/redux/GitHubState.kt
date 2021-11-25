package io.shortcut.features.github.redux

data class GitHubState(
    val issues: String = "",
    val status: Status = Status.IDLE
) {

    enum class Status {
        IDLE, PENDING
    }
}
