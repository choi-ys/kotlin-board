package io.example.board.service

import io.example.board.config.test.MockingTestConfig
import io.example.board.domain.dto.request.LoginRequest
import io.example.board.repository.MemberRepository
import io.example.board.util.generator.MemberGenerator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.springframework.context.annotation.Import
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*

@DisplayName("Service:Login")
@Import(MemberGenerator::class)
internal class LoginServiceTest : MockingTestConfig(){

    @Mock
    lateinit var passwordEncoder: PasswordEncoder

    @Mock
    lateinit var memberRepository: MemberRepository

    @InjectMocks
    lateinit var loginService: LoginService

    @Test
    @DisplayName("로그인")
    fun login(){
        // Given
        val member = MemberGenerator.generateMemberEntity()
        val loginRequest = LoginRequest(email = member.email, password = member.password)
        given(memberRepository.findByEmail(member.email)).willReturn(Optional.of(member))
        given(passwordEncoder.matches(loginRequest.password, member.password)).willReturn(true)

        // When
        val loginUser = loginService.login(loginRequest)

        // Then
        verify(memberRepository, times(1)).findByEmail(member.email)
        verify(passwordEncoder, times(1)).matches(loginRequest.password, member.password)

        assertEquals(loginUser.email, member.email)
        assertEquals(loginUser.nickname, member.nickname)
    }

    @Test
    @DisplayName("로그인 실패 : 요청에 해당하는 사용자가 없는경우")
    fun loginFail_causeUsernameNotfound(){
        // Given
        val loginRequest = LoginRequest(email = "test@naver.com", password = "password")

        // When & Then
        val expectedException = assertThrows(UsernameNotFoundException::class.java) {
            loginService.login(loginRequest)
        }
        assertEquals(expectedException.message, "요청에 해당하는 사용자가 없습니다.")
    }

    @Test
    @DisplayName("로그인 실패 : 비밀번호가 일치하지 않는 경우")
    fun loginFail_causePasswordNotCorrect(){
        // Given
        val member = MemberGenerator.generateMemberEntity()
        val loginRequest = LoginRequest(member.email, member.password)

        given(memberRepository.findByEmail(member.email)).willReturn(Optional.of(member))

        // When & Then
        val expectedException = assertThrows(Exception::class.java) {
            loginService.login(loginRequest)
        }

        verify(memberRepository, times(1)).findByEmail(member.email)
        verify(passwordEncoder, times(1)).matches(loginRequest.password, member.password)
        assertEquals(expectedException::class.java.simpleName, SecurityException::class.java.simpleName)
        assertEquals(expectedException.message, "로그인에 실패하였습니다. 로그인 정보를 다시 확인해 주세요.")
    }
}