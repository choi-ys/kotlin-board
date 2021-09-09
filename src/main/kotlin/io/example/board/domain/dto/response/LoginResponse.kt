package io.example.board.domain.dto.response

import io.example.board.domain.vo.login.token.Token

data class LoginResponse(
    val id: Long,
    val email: String,
    val nickname: String,
    val token : Token
)
