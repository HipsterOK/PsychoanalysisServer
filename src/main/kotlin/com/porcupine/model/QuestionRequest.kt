package com.porcupine.model

import kotlinx.serialization.Serializable

@Serializable
data class QuestionRequest(
    val text: String
)