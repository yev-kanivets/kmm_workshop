package io.shortcut.features.github.repository

import io.ktor.client.*
import io.ktor.client.request.*
import io.shortcut.network.HttpClientFactory
import io.shortcut.network.request

internal class GitHubRepository(
    private val httpClient: HttpClient = HttpClientFactory().create()
) {

    private val user = "yev-kanivets"
    private val repo = "kmm_workshop"

    suspend fun getIssues() = request {
        httpClient.get<String>(path = "repos/$user/$repo/issues")
    }
}
