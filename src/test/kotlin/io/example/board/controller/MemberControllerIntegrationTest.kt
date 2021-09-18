package io.example.board.controller

import io.example.board.config.test.IntegrationTestConfig
import io.example.board.domain.dto.request.SignupRequest
import io.example.board.domain.entity.rdb.member.MemberRole
import io.example.board.util.generator.MemberGenerator
import io.example.board.util.generator.TokenGenerator
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@DisplayName("API:Member[Integration]")
@Import(MemberGenerator::class, TokenGenerator::class)
internal class MemberControllerIntegrationTest : IntegrationTestConfig() {

    @Autowired
    lateinit var memberGenerator: MemberGenerator

    @Autowired
    lateinit var tokenGenerator: TokenGenerator

    private val SIGNUP_URL = "/member/signup"
    private val ROLES_URL = "/member/roles"

    @Test
    @DisplayName("[200:POST]회원 가입")
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
    @DisplayName("[400:POST]회원 가입 실패(값이 없는 요청)")
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
    @DisplayName("[400:POST]회원 가입 실패(값이 옳바르지 않은 요청)")
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
    @DisplayName("[400:POST]회원 가입 실패(유효하지 못한 요청)")
    fun signup_Fail_CauseIllegalArgument() {
        val signupRequest = SignupRequest("choi-ys", "project.log.062", "password", "noel")

        // When
        val resultAction = this.post(SIGNUP_URL, signupRequest)

        // Then
        resultAction.andDo(print())
            .andExpect(status().isBadRequest)
    }

    @Test
    @DisplayName("[415:POST]회원 가입 실패(잘못된 Media Type 요청)")
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
    @DisplayName("[500:POST]회원 가입 실패(이메일 중복)")
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

    @Test
    @DisplayName("[200:GET]권한 목록 조회")
    fun roles() {
        // Given
        val bearerToken = TokenGenerator.makeBearerToken(tokenGenerator.generateToken().accessToken)

        // When
        val resultActions = this.mockMvc.perform(
            MockMvcRequestBuilders.get(ROLES_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, bearerToken)
        )

        // Then
        resultActions.andDo(print())
            .andExpect(status().isOk)
    }

    @Test
    @DisplayName("[403:GET]권한 목록 조회")
    fun roles_Fail_CauseNoCredentials() {
        // When
        val resultActions = this.get(ROLES_URL)

        // Then TODO 자격증명 없는 요청의 403 응답 구조(timestamp, message, etc) 처리
        resultActions.andDo(print())
            .andExpect(status().isForbidden)
    }

    @Test
    @DisplayName("[200:POST]회원 권한 추가")
    fun addRoles() {
        // Given
        val savedMember = memberGenerator.savedMember()
        val bearerToken = tokenGenerator.getBearerToken(savedMember)
        val additionRoles = setOf(MemberRole.ADMIN, MemberRole.SYSTEM_ADMIN)

        // When
        val resultActions =
            this.mockMvc.perform(post(ROLES_URL + "/" + savedMember.id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, bearerToken)
                .content(this.objectMapper.writeValueAsBytes(additionRoles))
            )

        // Then
        resultActions.andDo(print())
            .andExpect(status().isOk)
    }

    @Test
    @DisplayName("[403:POST]회원 권한 추가")
    fun addRoles_Fail_CauseNoCredentials() {
        // Given
        val savedMember = memberGenerator.savedMember()
        val additionRoles = setOf(MemberRole.ADMIN, MemberRole.SYSTEM_ADMIN)

        // When
        val resultActions =
            this.mockMvc.perform(post(ROLES_URL + "/" + savedMember.id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsBytes(additionRoles))
            )

        // Then
        resultActions.andDo(print())
            .andExpect(status().isForbidden)
    }

    @Test
    @DisplayName("[200:DELETE]회원 권한 삭제")
    fun removeRoles() {
        // Given
        val savedMember = memberGenerator.savedMember()
        val bearerToken = tokenGenerator.getBearerToken(savedMember)
        val additionRoles = setOf(MemberRole.ADMIN, MemberRole.SYSTEM_ADMIN)

        // When
        val resultActions =
            this.mockMvc.perform(delete(ROLES_URL + "/" + savedMember.id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, bearerToken)
                .content(this.objectMapper.writeValueAsBytes(additionRoles))
            )

        // Then
        resultActions.andDo(print())
            .andExpect(status().isOk)
    }

    @Test
    @DisplayName("[403:DELETE]회원 권한 삭제")
    fun removeRoles_Fail_CauseNoCredentials() {
        // Given
        val savedMember = memberGenerator.savedMember()
        val additionRoles = setOf(MemberRole.ADMIN, MemberRole.SYSTEM_ADMIN)

        // When
        val resultActions =
            this.mockMvc.perform(delete(ROLES_URL + "/" + savedMember.id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsBytes(additionRoles))
            )

        // Then
        resultActions.andDo(print())
            .andExpect(status().isForbidden)
    }
}