package io.example.board.domain.dto.request

data class SignupRequest(
    val name: String,
    val email: String,
    val password: String,
    val nickname: String
)
