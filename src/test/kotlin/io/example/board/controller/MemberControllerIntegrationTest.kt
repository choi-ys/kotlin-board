package io.example.board.controller

import io.example.board.config.test.IntegrationTestConfig
import io.example.board.domain.dto.response.SignupResponse
import io.example.board.util.generator.MemberGenerator
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@DisplayName("Controller:Member")
internal class MemberControllerIntegrationTest : IntegrationTestConfig(){

    private val SIGNUP_URL = "/member/signup"
    private val signupRequest = MemberGenerator.generateSignupRequest()
    private val signupResponse = SignupResponse(id = 1L, name = signupRequest.name, email = signupRequest.email, nickname = signupRequest.nickname)

    @Test
    @DisplayName("API:회원 가입")
    @Throws(Exception::class)
    fun signup(){
        // When
        val resultAction = this.post(SIGNUP_URL, signupRequest)

        // Then
        resultAction.andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("id").value(signupResponse.id))
            .andExpect(jsonPath("name").value(signupResponse.name))
            .andExpect(jsonPath("email").value(signupResponse.email))
            .andExpect(jsonPath("nickname").value(signupResponse.nickname))
    }

    @Test
    @DisplayName("API:회원 가입 실패(값이 옳바르지 않은 요청)")
    fun signup_Fail_CauseInvalidArgument(){
        // Given
        val errorMessage = "이미 존재하는 이메일 입니다."

        // When
        val resultAction = this.post(SIGNUP_URL, signupRequest)

        // Then
        resultAction.andDo(print())
    }

    @Test
    @DisplayName("API:회원 가입 실패(유효하지 못한 요청)")
    fun signup_Fail_CauseIllegalArgument(){

    }
}