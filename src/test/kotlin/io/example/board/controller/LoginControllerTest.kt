package io.example.board.controller

import io.example.board.config.test.IntegrationTestConfig
import io.example.board.domain.dto.request.LoginRequest
import io.example.board.domain.dto.request.RefreshTokenRequest
import io.example.board.domain.dto.request.SignupRequest
import io.example.board.service.MemberService
import io.example.board.util.generator.MemberGenerator
import io.example.board.util.generator.TokenGenerator
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * @author : choi-ys
 * @date : 2021/09/03 2:49 오후
 */
@DisplayName("Controller:Login")
internal class LoginControllerTest : IntegrationTestConfig() {

    @Autowired
    lateinit var memberService: MemberService

    @Autowired
    lateinit var tokenGenerator: TokenGenerator

    @Test
    @DisplayName("API:로그인")
    fun login() {
        // Given
        val member = MemberGenerator.generateMemberEntity()
        val signupRequest = SignupRequest(member.name, member.email, member.password, member.nickname)
        memberService.signup(signupRequest)

        val loginRequest = LoginRequest(member.email, member.password)

        // When
        val resultActions = mockMvc.perform(
            post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(loginRequest))
        )

        // Then
        resultActions.andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("id").exists())
            .andExpect(jsonPath("email").value(member.email))
            .andExpect(jsonPath("nickname").value(member.nickname))
            .andExpect(jsonPath("token.accessToken").exists())
            .andExpect(jsonPath("token.refreshToken").exists())
            .andExpect(jsonPath("token.accessExpired").exists())
            .andExpect(jsonPath("token.refreshExpired").exists())
    }

    @Test
    @DisplayName("API:토큰 갱신")
    fun refresh() {
        // Given
        val generateToken = tokenGenerator.generateToken();
        val refreshTokenRequest = RefreshTokenRequest(generateToken.accessToken, generateToken.refreshToken)

        // When
        val resultActions = mockMvc.perform(
            post("/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, TokenGenerator.makeBearerToken(generateToken.refreshToken))
                .content(objectMapper.writeValueAsBytes(refreshTokenRequest))
        )

        // Then
        resultActions.andDo(print())
            .andExpect(status().isOk)
    }
}