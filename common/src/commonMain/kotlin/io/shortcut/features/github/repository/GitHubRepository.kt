package io.shortcut.features.github.repository

import io.ktor.client.*
import io.ktor.client.request.*
import io.shortcut.features.github.entity.Issue
import io.shortcut.network.HttpClientFactory
import io.shortcut.network.request

internal class GitHubRepository(
    private val httpClient: HttpClient = HttpClientFactory().create()
) {

    private val user = "yev-kanivets"
    private val repo = "kmm_workshop"

    suspend fun getIssues() = request {
        httpClient.get<List<Issue>>(path = "repos/$user/$repo/issues")
    }

    suspend fun updateIssue(
        issueNumber: Int,
        title: String,
        body: String
    ) = request {
        httpClient.patch<Issue>(
            path = "repos/$user/$repo/issues/$issueNumber",
            body = mapOf(
                "title" to title,
                "body" to body
            )
        ) {
            header("Content-Type", "application/json")
            header("Authorization", "Bearer your_token_here")
        }
    }
}
