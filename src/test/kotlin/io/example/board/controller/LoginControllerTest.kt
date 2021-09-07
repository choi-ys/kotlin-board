package io.example.board.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.example.board.config.test.EnableMockMvc
import io.example.board.domain.dto.request.LoginRequest
import io.example.board.domain.dto.request.SignupRequest
import io.example.board.service.MemberService
import io.example.board.util.generator.MemberGenerator
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

/**
 * @author : choi-ys
 * @date : 2021/09/03 2:49 오후
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableMockMvc
@AutoConfigureMockMvc
@Import(MemberGenerator::class)
@DisplayName("Controller:Login")
@Transactional
internal class LoginControllerTest {

    @Autowired
    lateinit var memberService: MemberService

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

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
    }
}