package io.example.board.repository

import io.example.board.config.test.JpaTestConfig
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
    fun save(){
        // Given
        val generatedMemberEntity = MemberGenerator.generateMemberEntity()

        // When
        val savedMemberEntity = memberRepository.save(generatedMemberEntity)
        print(savedMemberEntity)
        flush()

        // Then
        assertNotEquals(savedMemberEntity.id, 0L)
        assertEquals(generatedMemberEntity, savedMemberEntity)
        assertEquals(savedMemberEntity.status, MemberStatus.UNCERTIFIED)
        assertNotEquals(savedMemberEntity.createdBy, null)
        assertNotEquals(savedMemberEntity.updatedBy, null)
        assertNotEquals(savedMemberEntity.createdAt, null)
        assertNotEquals(savedMemberEntity.updatedAt, null)
    }

    @Test
    @DisplayName("회원 객체 조회")
    fun findById(){
        // Given
        val savedMemberEntity = memberRepository.save(MemberGenerator.generateMemberEntity())
        flushAndClear()

        // When
        val selectedMemberEntity = memberRepository.findById(savedMemberEntity.id).orElseThrow()

        // Then
        assertEquals(savedMemberEntity.id, selectedMemberEntity.id)
    }

    @Test
    @DisplayName("회원 속성 수정")
    fun updateByDirtyChecking(){
        // Given
        val savedMemberEntity = memberRepository.save(MemberGenerator.generateMemberEntity())

        val newName = "최용식"
        savedMemberEntity.updateName(newName)

        flushAndClear()

        // When
        val selectedMemberEntity = memberRepository.findById(savedMemberEntity.id).orElseThrow()

        // Then
        assertEquals(selectedMemberEntity.name, newName)
    }

    @Test
    @DisplayName("회원 객체 삭제")
    fun delete(){
        // Given
        val savedMemberEntity = memberRepository.save(MemberGenerator.generateMemberEntity())

        // When
        memberRepository.delete(savedMemberEntity)
        flushAndClear()

        // Then : check thrown exception
        assertThrows(NoSuchElementException::class.java) {
            memberRepository.findById(savedMemberEntity.id).get()
        }
        // Then : check query result as null
        assertEquals(memberRepository.findByIdOrNull(savedMemberEntity.id), null)
    }
}