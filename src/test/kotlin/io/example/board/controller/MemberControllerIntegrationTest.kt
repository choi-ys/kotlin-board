package io.example.board.controller

import io.example.board.config.test.IntegrationTestConfig
import io.example.board.domain.dto.request.SignupRequest
import io.example.board.util.generator.MemberGenerator
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@DisplayName("Controller:Member")
@Import(MemberGenerator::class)
internal class MemberControllerIntegrationTest : IntegrationTestConfig() {

    @Autowired
    lateinit var memberGenerator: MemberGenerator

    private val SIGNUP_URL = "/member/signup"

    @Test
    @DisplayName("API:[200]회원 가입")
    fun signup() {
        val signupRequest = MemberGenerator.signupRequest()

        // When
        val resultAction = this.post(SIGNUP_URL, signupRequest)

        // Then
        resultAction.andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("id").exists())
            .andExpect(jsonPath("name").value(signupRequest.name))
            .andExpect(jsonPath("email").value(signupRequest.email))
            .andExpect(jsonPath("nickname").value(signupRequest.nickname))
    }

    @Test
    @DisplayName("API:[400]회원 가입 실패(값이 없는 요청)")
    fun signup_Fail_CauseNoArgument() {
        // When
        val resultAction = mockMvc.perform(
            post(SIGNUP_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )

        // Then
        resultAction.andDo(print())
            .andExpect(status().isBadRequest)
    }

    @Test
    @DisplayName("API:[400]회원 가입 실패(값이 옳바르지 않은 요청)")
    fun signup_Fail_CauseInvalidArgument() {
        // Given
        val signupRequest = SignupRequest("", "", "", "")

        // When
        val resultAction = this.post(SIGNUP_URL, signupRequest)

        // Then
        resultAction.andDo(print())
            .andExpect(status().isBadRequest)
    }

    @Test
    @DisplayName("API:[400]회원 가입 실패(유효하지 못한 요청)")
    fun signup_Fail_CauseIllegalArgument() {
        val signupRequest = SignupRequest("choi-ys", "project.log.062", "password", "noel")

        // When
        val resultAction = this.post(SIGNUP_URL, signupRequest)

        // Then
        resultAction.andDo(print())
            .andExpect(status().isBadRequest)
    }

    @Test
    @DisplayName("API:[415]회원 가입 실패(잘못된 Media Type 요청)")
    fun signup_Fail_CauseInValidMimeType() {
        // When
        val resultAction = mockMvc.perform(
            post(SIGNUP_URL)
                .contentType(objectMapper.writeValueAsString(""))
        )

        // Then
        resultAction.andDo(print())
            .andExpect(status().`is`(415))
    }


    @Test
    @DisplayName("API:[500]회원 가입 실패(이메일 중복)")
    fun signup_Fail_CauseDuplicatedEmail() {
        // Given
        val savedMember = memberGenerator.savedMember()
        val signupRequest = SignupRequest(
            name = savedMember.name,
            email = savedMember.email,
            password = savedMember.password,
            nickname = savedMember.nickname
        )
        val errorMessage = "이미 존재하는 이메일 입니다."

        // When
        val resultAction = this.post(SIGNUP_URL, signupRequest)

        // Then
        resultAction.andDo(print())
            .andExpect(status().isInternalServerError)
            .andExpect(jsonPath("message").value(errorMessage))
            .andExpect(jsonPath("request").value(signupRequest.email))
    }
}