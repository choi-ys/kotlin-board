package io.example.board.domain.dto.request

data class RefreshTokenRequest(
    val accessToken: String,
    val refreshToken: String,
)
