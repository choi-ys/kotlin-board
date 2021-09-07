package io.example.board.domain.entity.rdb.member

import io.example.board.util.generator.MemberGenerator
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.stream.Collectors

@DisplayName("Entity: Member")
internal class MemberTest {

    @Test
    @DisplayName("회원 객체 생성")
    fun member() {
        // Given
        var name: String = "최용석"
        var email: String = "test@naver.com"
        var password: String = "password"
        var nickname: String = "김턱상"

        // When
        val member: Member = Member(name, email, password, nickname)

        // Then
        assertEquals(member.id, 0L)
        assertEquals(member.name, name)
        assertEquals(member.email, email)
        assertEquals(member.password, password)
        assertEquals(member.nickname, nickname)
        assertEquals(member.status, MemberStatus.UNCERTIFIED)
    }

    @Test
    @DisplayName("회원 정보 변경")
    fun updateName() {
        // Given
        var member: Member = MemberGenerator.generateMemberEntity()
        var newName = "updated name"
        var newEmail = "updated@naver.com"
        var newPassword = "updated password"
        var newNickname = "updated niczkname"

        // When
        member.updateName(newName)
        member.updateEmail(newEmail)
        member.updatePassword(newPassword)
        member.updateNickname(newNickname)

        // Then
        assertEquals(member.id, 0L)
        assertEquals(member.name, newName)
        assertEquals(member.email, newEmail)
        assertEquals(member.password, newPassword)
        assertEquals(member.nickname, newNickname)
        assertEquals(member.status, MemberStatus.UNCERTIFIED)
    }

    @Test
    @DisplayName("MemberRole을 SimpleGrantedAuthority로 변경")
    fun mapToSimpleGrantedAuthority() {
        // Given
        val member = MemberGenerator.generateMemberEntity()

        // When
        val simpleGrantedAuthoritySet = member.mapToSimpleGrantedAuthority()

        // Then
        assertEquals(
            simpleGrantedAuthoritySet,
            member.roles.stream().map { SimpleGrantedAuthority("ROLE_" + it.name) }.collect(Collectors.toSet())
        )
    }

    @Test
    @DisplayName("회원 권한 추가")
    fun addRoles() {
        // Given
        val member = MemberGenerator.generateMemberEntity()
        val additionRoles = setOf(MemberRole.ADMIN, MemberRole.SYSTEM_ADMIN)

        // When
        member.addRoles(additionRoles)

        // Then
        assertTrue(member.roles.containsAll(additionRoles))
    }

    @Test
    @DisplayName("회원 권한 제거")
    fun removeRoles() {
        // Given
        val member = MemberGenerator.generateMemberEntity()
        member.addRoles(setOf(MemberRole.ADMIN, MemberRole.SYSTEM_ADMIN))
        val removalRoles = setOf(MemberRole.MEMBER, MemberRole.ADMIN)

        // When
        member.removeRoles(removalRoles)

        // Then
        assertFalse(member.roles.containsAll(removalRoles))
    }

    @Test
    @DisplayName("모든 권한 제거 시 예외")
    fun exceptionByRemoveAllRoles() {
        // Given
        val member = MemberGenerator.generateMemberEntity()

        // When
        val exception = assertThrows(IllegalArgumentException::class.java) {
            member.removeRoles(member.roles)
        }

        // Then
        assertAll(
            { assertEquals(exception.javaClass.simpleName, IllegalArgumentException::class.simpleName) },
            { assertEquals(exception.message, "최소 하나 이상의 권한이 존재해야 합니다.") }
        )
    }
}