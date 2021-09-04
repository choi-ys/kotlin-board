package io.example.board.repository

import io.example.board.config.test.JpaTestConfig
import io.example.board.config.test.assertions.ObjectAssertions.Companion.assertMember
import io.example.board.domain.entity.rdb.common.EventType
import io.example.board.domain.entity.rdb.history.MemberHistory
import io.example.board.domain.entity.rdb.member.MemberStatus
import io.example.board.util.ApplicationContextUtil
import io.example.board.util.generator.MemberGenerator
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
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

    @Autowired
    lateinit var memberHistoryRepository: MemberHistoryRepository

    @Test
    @DisplayName("회원 객체 저장")
    fun save() {
        // Given
        val givenMember = MemberGenerator.generateMemberEntity()

        // When
        val expected = memberRepository.save(givenMember)
        val histories = memberHistoryRepository.findByMemberIdOrderByCreatedAtDesc(expected.id)
        flush()

        // Then
        assertAll(
            { assertMember(expected, givenMember) },
            { assertEquals(givenMember, expected, "동일 트랜잭션 내에서 객체의 동일성 보장 여부 확인") },
            { assertEquals(expected.status, MemberStatus.UNCERTIFIED, "최초 생성 회원의 미인증 상태 여부 확인") },
            {
                assertTrue(
                    histories.stream()
                        .map(MemberHistory::eventType)
                        .toArray().first().equals(EventType.PERSIST),
                    "Member Entity에 등록된 Listner로 처리된 이력 객체의 첫 번째 EventType값 PERSIST 여부 확인"
                )
            }
        )
    }

    @Test
    @DisplayName("회원 객체 조회")
    fun findById() {
        // Given
        val savedMember = memberRepository.save(MemberGenerator.generateMemberEntity())
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
        val savedMember = memberRepository.save(MemberGenerator.generateMemberEntity())

        val newName = "최용식"
        savedMember.updateName(newName)

        flushAndClear()

        // When
        val expected = memberRepository.findById(savedMember.id).orElseThrow()

        // Then
        assertAll(
            { assertMember(expected, savedMember) },
            { assertEquals(expected.name, newName) }
        )

    }

    @Disabled
    @Test
    @DisplayName("회원 객체 삭제")
    fun delete() {
        // Given
        val savedMember = memberRepository.save(MemberGenerator.generateMemberEntity())

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
}