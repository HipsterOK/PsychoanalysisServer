package com.porcupine.model

import kotlinx.serialization.Serializable

@Serializable
data class QuestionResponse(
    val id: Long,
    val text: String
)