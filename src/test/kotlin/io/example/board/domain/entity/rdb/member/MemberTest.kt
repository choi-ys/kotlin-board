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
        val name = "최용석"
        val email = "test@naver.com"
        val password = "password"
        val nickname = "김턱상"

        // When
        val member = Member(name = name, email = email, password = password, nickname = nickname)

        // Then
        assertAll(
            { assertEquals(member.id, 0L) },
            { assertEquals(member.name, name) },
            { assertEquals(member.email, email) },
            { assertEquals(member.password, password) },
            { assertEquals(member.nickname, nickname) },
            { assertEquals(member.status, MemberStatus.UNCERTIFIED, "생성자에서 제외된 회원 인증 상태 항목의 기본 인증 상태를 확인") },
            { assertEquals(member.createdBy, null, "Auditor를 통해 설정되는 생성주체 정보의 null 여부를 확인") },
            { assertEquals(member.updatedBy, null, "Auditor를 통해 설정되는 수정주체 정보의 null 여부를 확인") },
            { assertEquals(member.createdAt, null, "Auditor를 통해 설정되는 생성일자 정보의 null 여부를 확인") },
            { assertEquals(member.updatedAt, null, "Auditor를 통해 설정되는 수정일자 정보의 null 여부를 확인") }
        )
    }

    @Test
    @DisplayName("회원 정보 변경")
    fun updateName() {
        // Given
        val member: Member = MemberGenerator.member()
        val newName = "updated name"
        val newEmail = "updated@naver.com"
        val newPassword = "updated password"
        val newNickname = "updated nickname"

        // When
        member.updateName(newName)
        member.updateEmail(newEmail)
        member.updatePassword(newPassword)
        member.updateNickname(newNickname)

        // Then
        assertAll(
            { assertEquals(member.id, 0L) },
            { assertEquals(member.name, newName) },
            { assertEquals(member.email, newEmail) },
            { assertEquals(member.password, newPassword) },
            { assertEquals(member.nickname, newNickname) },
            { assertEquals(member.status, MemberStatus.UNCERTIFIED, "생성자에서 제외된 회원 인증 상태 항목의 기본 인증 상태를 확인") },
            { assertEquals(member.createdBy, null, "Auditor를 통해 설정되는 생성주체 정보의 null 여부를 확인") },
            { assertEquals(member.updatedBy, null, "Auditor를 통해 설정되는 수정주체 정보의 null 여부를 확인") },
            { assertEquals(member.createdAt, null, "Auditor를 통해 설정되는 생성일자 정보의 null 여부를 확인") },
            { assertEquals(member.updatedAt, null, "Auditor를 통해 설정되는 수정일자 정보의 null 여부를 확인") }
        )
    }

    @Test
    @DisplayName("회원 권한 추가")
    fun addRoles() {
        // Given
        val member = MemberGenerator.member()
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
        val member = MemberGenerator.member()
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
        val member = MemberGenerator.member()

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

    @Test
    @DisplayName("[객체 매핑] Set<MemberRole> -> Set<SimpleGrantedAuthority>")
    fun mapToSimpleGrantedAuthority() {
        // Given
        val member = MemberGenerator.member()

        // When
        val simpleGrantedAuthoritySet = member.mapToSimpleGrantedAuthority()

        // Then
        assertEquals(
            simpleGrantedAuthoritySet,
            member.roles.stream().map { SimpleGrantedAuthority("ROLE_" + it.name) }.collect(Collectors.toSet())
        )
    }
}