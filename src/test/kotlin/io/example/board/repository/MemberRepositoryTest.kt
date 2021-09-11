package io.example.board.repository

import io.example.board.config.test.JpaTestConfig
import io.example.board.config.test.assertions.ObjectAssertions.Companion.assertMember
import io.example.board.domain.entity.rdb.member.MemberRole
import io.example.board.domain.entity.rdb.member.MemberStatus
import io.example.board.util.ApplicationContextUtil
import io.example.board.util.generator.MemberGenerator
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.data.repository.findByIdOrNull

@DisplayName("Repository:Member")
@Import(ApplicationContextUtil::class)
internal class MemberRepositoryTest : JpaTestConfig() {

    @Autowired
    lateinit var memberRepository: MemberRepository

    @Test
    @DisplayName("회원 객체 저장")
    fun save() {
        // Given
        val givenMember = MemberGenerator.member()

        // When
        val expected = memberRepository.save(givenMember)
        flush()

        // Then
        assertAll(
            { assertMember(expected, givenMember) },
            { assertEquals(givenMember, expected, "동일 트랜잭션 내에서 객체의 동일성 보장 여부 확인") },
            { assertEquals(expected.status, MemberStatus.UNCERTIFIED, "최초 생성 회원의 미인증 상태 여부 확인") },
        )
    }

    @Test
    @DisplayName("회원 객체 조회")
    fun findById() {
        // Given
        val savedMember = memberRepository.save(MemberGenerator.member())
        flushAndClear()

        // When
        val expected = memberRepository.findById(savedMember.id).orElseThrow()

        // Then
        assertAll(
            { assertMember(expected, savedMember) }
        )
    }

    @Test
    @DisplayName("회원 속성 수정")
    fun updateByDirtyChecking() {
        // Given
        val savedMember = memberRepository.save(MemberGenerator.member())

        val newName = "최용식"
        savedMember.updateName(newName)

        flushAndClear()

        // When
        val expected = memberRepository.findById(savedMember.id).orElseThrow()

        // Then
        assertAll(
            { assertMember(expected, savedMember) },
            { assertEquals(expected.name, newName) },
        )
    }

    @Test
    @DisplayName("회원 객체 삭제")
    fun delete() {
        // Given
        val savedMember = memberRepository.save(MemberGenerator.member())

        // When
        memberRepository.delete(savedMember)
        flushAndClear()

        // Then : check thrown exception
        assertThrows(NoSuchElementException::class.java) {
            memberRepository.findById(savedMember.id).get()
        }
        // Then : check query result as null
        assertEquals(memberRepository.findByIdOrNull(savedMember.id), null)
    }

    @Test
    @DisplayName("회원 권한 추가")
    fun addRoles() {
        // Given
        val savedMember = memberRepository.saveAndFlush(MemberGenerator.member())
        val additionRoles = setOf(MemberRole.ADMIN, MemberRole.SYSTEM_ADMIN)

        // When
        savedMember.addRoles(additionRoles)
        flush()

        // Then
        assertTrue(savedMember.roles.containsAll(additionRoles))
    }

    @Test
    @DisplayName("회원 권한 제거")
    fun removeRoles() {
        // Given
        val savedMember = memberRepository.saveAndFlush(MemberGenerator.member())
        val additionRoles = setOf(MemberRole.ADMIN, MemberRole.SYSTEM_ADMIN)
        savedMember.addRoles(additionRoles)
        flush()
        val removalRoles = setOf(MemberRole.MEMBER, MemberRole.ADMIN)

        // When
        savedMember.removeRoles(removalRoles)
        flush()

        // Then
        assertFalse(savedMember.roles.containsAll(removalRoles))
    }

    @Test
    @DisplayName("모든 권한 제거 시 예외")
    fun exceptionByRemoveAllRoles() {
        // Given
        val savedMember = memberRepository.saveAndFlush(MemberGenerator.member())

        // When
        val exception = assertThrows(IllegalArgumentException::class.java) {
            savedMember.removeRoles(savedMember.roles)
        }

        // Then
        assertAll(
            { assertEquals(exception.javaClass.simpleName, IllegalArgumentException::class.java.simpleName) },
            { assertEquals(exception.message, "최소 하나 이상의 권한이 존재해야 합니다.") }
        )
    }
}