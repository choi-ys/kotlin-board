package io.example.board.domain.dto.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class SignupRequest(

    @NotBlank(message = "이름은 필수 입력사항 입니다.")
    @Size(min = 1, max= 10, message = "1~10자 이내로 입력하세요.")
    val name: String,

    @Email(message = "옳바른 이메일 형식이 아닙니다.")
    val email: String,

    @NotBlank(message = "비밀번호는 필수 입력사항 입니다.")
    @Size(min = 8, max= 25, message = "8~20자 이내로 입력하세요.")
    val password: String,

    @NotBlank(message = "닉네임 항목은 필수 입력 사항입니다.")
    @Size(min = 1, max= 10, message = "1~10자 이내로 입력하세요")
    val nickname: String
)
