package io.shortcut.features.space.repository

import io.ktor.client.*
import io.ktor.client.request.*
import io.shortcut.features.space.entity.PeopleInSpace
import io.shortcut.network.HttpClientFactory
import io.shortcut.network.request

internal class SpaceRepository(
        private val httpClient: HttpClient = HttpClientFactory().create()
) {

    suspend fun getPeopleInSpace() = request {
        httpClient.get<PeopleInSpace>(path = "astros.json")
    }
}
