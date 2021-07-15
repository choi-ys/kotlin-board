package io.example.board.domain.dto.response

data class SignupResponse(
    val id: Long,
    val name: String,
    val email: String,
    val nickname: String
)
