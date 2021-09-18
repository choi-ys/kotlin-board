package io.example.board.service

import io.example.board.aspect.exception.CommonException
import io.example.board.config.test.MockingTestConfig
import io.example.board.domain.dto.request.MemberCertifyRequest
import io.example.board.domain.dto.request.SignupRequest
import io.example.board.domain.entity.rdb.member.Member
import io.example.board.domain.entity.rdb.member.MemberRole
import io.example.board.domain.entity.redis.MailCache
import io.example.board.repository.MailCacheRepo
import io.example.board.repository.MemberRepo
import io.example.board.util.generator.MemberGenerator
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.springframework.context.annotation.Import
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*

/**
 * 관심사 : 메소드의 실제 내부 동작은 실행되지 않고, Mocking과 stubbing을 통해 상황만 설정
 *  - Exception 발생 여부
 *  - 연관관계를 가진 계층과의 상호작용 여부(*Repository, *Service)
 *  관심 제외
 *  - 외부 Repository및 Service 연동 및 수행 여부
 */
@DisplayName("Service:Member")
@Import(MemberGenerator::class)
class MemberServiceTest : MockingTestConfig() {

    @Mock
    lateinit var passwordEncoder: PasswordEncoder

    @Mock
    lateinit var memberRepo: MemberRepo

    @Mock
    lateinit var mailService: MailService

    @Mock
    lateinit var mailCacheRepo: MailCacheRepo

    @InjectMocks
    private lateinit var memberService: MemberService

    @Test
    @DisplayName("회원 가입")
    fun signup() {
        // Given
        val signupRequest: SignupRequest = MemberGenerator.signupRequest()
        val member = MemberGenerator.member()

        given(passwordEncoder.encode(signupRequest.password)).willReturn("encoded password")
        given(memberRepo.save(any(Member::class.java))).willReturn(member)

        // When
        val signupResponse = memberService.signup(signupRequest)

        // Then
        verify(memberRepo, times(1)).existsByEmail(signupRequest.email)
        verify(passwordEncoder, times(1)).encode(any(String::class.java))
        verify(memberRepo, times(1)).save(any(Member::class.java))

        assertEquals(signupResponse.email, signupRequest.email)
        assertEquals(signupResponse.name, signupRequest.name)
        assertEquals(signupResponse.nickname, signupRequest.nickname)
    }

    @Test
    @DisplayName("회원 가입 실패 : 이메일 중복")
    fun signup_duplicationEmailException() {
        // Given
        val signupRequest = MemberGenerator.signupRequest()
        given(memberRepo.existsByEmail(signupRequest.email)).willReturn(true)

        // When & Then
        val expectedException = assertThrows(CommonException::class.java) {
            memberService.signup(signupRequest)
        }
        verify(memberRepo, times(1)).existsByEmail(signupRequest.email)
        assertEquals(expectedException.javaClass.simpleName, CommonException::class.simpleName)
    }

    @Test
    @DisplayName("회원 권한 추가")
    fun addRoles() {
        // Given
        val member = MemberGenerator.member()
        val additionRoles = setOf(MemberRole.ADMIN)
        given(memberRepo.findById(member.id)).willReturn(Optional.of(member))

        // When
        memberService.addRoles(additionRoles, member.id)

        // Then
        verify(memberRepo, times(1)).findById(member.id)
        assertTrue(member.roles.containsAll(additionRoles))
    }

    @Test
    @DisplayName("회원 권한 삭제")
    fun removalRoles() {
        // Given
        val member = MemberGenerator.member()
        member.addRoles(setOf(MemberRole.ADMIN, MemberRole.SYSTEM_ADMIN))
        val removalRoles = setOf(MemberRole.MEMBER)

        given(memberRepo.findById(member.id)).willReturn(Optional.of(member))

        // When
        memberService.removeRoles(removalRoles, member.id)

        // Then
        verify(memberRepo, times(1)).findById(member.id)
        assertAll(
            { assertFalse(member.roles.containsAll(removalRoles)) }
        )
    }

    @Test
    @DisplayName("회원 권한 삭제")
    fun removalRoles_Fail_CauseAtLeastOneRoleMustExistException() {
        // Given
        val member = MemberGenerator.member()
        val removalRoles = setOf(MemberRole.MEMBER)

        given(memberRepo.findById(member.id)).willReturn(Optional.of(member))

        // When
        assertThrows(IllegalArgumentException::class.java) {
            memberService.removeRoles(removalRoles, member.id)
        }.let {
            assertEquals(it.javaClass.simpleName, IllegalArgumentException::class.java.simpleName)
            assertEquals(it.message, "최소 하나 이상의 권한이 존재해야 합니다.")
        }

        // Then
        verify(memberRepo, times(1)).findById(member.id)
        assertAll(
            { assertTrue(member.roles.containsAll(removalRoles)) }
        )
    }

    @Test
    @DisplayName("회원 인증 메일 발송")
    fun receiptCertify() {
        // Given
        val member = MemberGenerator.member()
        val certificationText = UUID.randomUUID().toString()
        val mailCache = MailCache(email = member.email, certificationText = certificationText)

        given(memberRepo.findById(1L)).willReturn(Optional.of(member))
        given(mailService.sendCertificationMail(member.email)).willReturn(certificationText)
        given(mailCacheRepo.save(any(MailCache::class.java))).willReturn(mailCache)

        // When
        val certifiedResponse = memberService.receiptCertify(1L)

        // Then
        verify(memberRepo, times(1)).findById(1L)
        verify(mailService, times(1)).sendCertificationMail(member.email)
        verify(mailCacheRepo, times(1)).save(mailCache)

        assertEquals(certifiedResponse.email, mailCache.email)
        assertEquals(certifiedResponse.certificationText, mailCache.certificationText)
    }

    @Test
    @DisplayName("회원 인증 메일 발송 실패 : 존재하지 않는 회원")
    fun receiptCertify_UsernameNotFoundException() {
        // Given
        given(memberRepo.findById(1L)).willReturn(Optional.empty())

        // When & Then
        val expectedException = assertThrows(CommonException::class.java) {
            memberService.receiptCertify(1L)
        }
        verify(memberRepo, times(1)).findById(any(Long::class.java))
        assertEquals(expectedException.javaClass.simpleName, CommonException::class.simpleName)
    }

    @Test
    @DisplayName("회원 인증")
    fun checkCertify() {
        // Given
        val member = MemberGenerator.member()
        val mailCache = MailCache(email = member.email, UUID.randomUUID().toString())
        given(memberRepo.findById(any(Long::class.java))).willReturn(Optional.of(member))
        given(mailCacheRepo.findById(member.email)).willReturn(Optional.of(mailCache))

        var memberCertifyRequest = MemberCertifyRequest(
            id = 1L,
            email = member.email,
            certificationTest = mailCache.certificationText
        )

        // When
        val checkCertify = memberService.checkCertify(memberCertifyRequest)

        // Then
        verify(memberRepo, times(1)).findById(1L)
        verify(mailCacheRepo, times(1)).findById(member.email)
        assertEquals(checkCertify, true)
    }

    @Test
    @DisplayName("회원 인증 실패 : 잘못된 요청")
    fun checkCertify_IllegalArgumentException() {
        // Given
        given(memberRepo.findById(any(Long::class.java))).willReturn(Optional.empty())

        var memberCertifyRequest = MemberCertifyRequest(
            id = 1L,
            email = "rcn115@naver.com",
            certificationTest = UUID.randomUUID().toString()
        )

        // When & Then
        val expectedException = assertThrows(CommonException::class.java) {
            memberService.checkCertify(memberCertifyRequest)
        }
        verify(memberRepo, times(1)).findById(any(Long::class.java))
        assertEquals(expectedException.javaClass.simpleName, CommonException::class.simpleName)
    }

    @Test
    @DisplayName("회원 인증 실패 : 인증 만료")
    fun checkCertify_expiredAuthenticationException() {
        // Given
        val member = MemberGenerator.member()
        given(memberRepo.findById(any(Long::class.java))).willReturn(Optional.of(member))
        given(mailCacheRepo.findById(any(String::class.java))).willReturn(Optional.empty())

        var memberCertifyRequest = MemberCertifyRequest(
            id = 1L,
            email = member.email,
            certificationTest = UUID.randomUUID().toString()
        )

        // When & Then
        val expectedException = assertThrows(CommonException::class.java) {
            memberService.checkCertify(memberCertifyRequest)
        }

        verify(memberRepo, times(1)).findById(any(Long::class.java))
        verify(mailCacheRepo, times(1)).findById(member.email)
        assertEquals(expectedException.javaClass.simpleName, CommonException::class.simpleName)

    }
}