package io.example.board.service

import io.example.board.config.base.BaseTestAnnotations
import io.example.board.domain.dto.request.SignupRequest
import io.example.board.domain.dto.response.SignupResponse
import io.example.board.domain.entity.Member
import io.example.board.repository.MemberRepository
import io.example.board.util.generator.MemberGenerator
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
@DisplayName("Service:Member[Integration]")
internal class MemberServiceIntegrationTest : BaseTestAnnotations(){

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    lateinit var memberRepository: MemberRepository

    @Autowired
    lateinit var memberService: MemberService

    @Test
    @DisplayName("회원 가입")
    fun signup() {
        // Given
        var signupRequest: SignupRequest = MemberGenerator.generateSignupRequest()

        // When
        val signupResponse: SignupResponse = memberService.signup(signupRequest)
        val signupMemberEntity: Member = memberRepository.findById(signupResponse.id).orElseThrow { IllegalArgumentException() }

        // Then
        assertNotEquals(signupResponse.id, 0)
        assertEquals(signupRequest.name, signupResponse.name)
        assertEquals(signupRequest.email, signupResponse.email)
        assertEquals(signupRequest.nickname, signupResponse.nickname)
        assertEquals(signupResponse.id, signupMemberEntity.id)
        assertEquals(passwordEncoder.matches(signupRequest.password, signupMemberEntity.password), true)
    }

    @Test
    @DisplayName("회원 가입 실패 : 중복")
    fun duplicatedEmailSignup(){
        // Given
        memberRepository.save(MemberGenerator.generateMemberEntity())
        var signupRequest: SignupRequest = MemberGenerator.generateSignupRequest()

        // When
        val expectedException = assertThrows(IllegalArgumentException::class.java) {
            memberService.signup(signupRequest)
        }

        assertEquals(expectedException::class.java.simpleName, IllegalArgumentException::class.java.simpleName)
        assertEquals(expectedException.message, "이미 존재하는 이메일 입니다.")
    }
}