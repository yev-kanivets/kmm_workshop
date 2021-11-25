package io.shortcut.features.github.entity

import kotlinx.serialization.Serializable

@Serializable
data class Issue(
    val number: Int,
    val title: String,
    val body: String
)
