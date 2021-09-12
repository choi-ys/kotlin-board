package io.example.board.domain.dto.request

import javax.validation.constraints.NotBlank

data class LoginRequest(
    @NotBlank(message = "이메일을 입력해주세요.")
    val email: String,

    @NotBlank(message = "비밀번호를 입력해주세요.")
    val password: String
)
