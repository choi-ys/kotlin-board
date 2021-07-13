package io.example.board.domain.entity

import io.example.board.util.generator.MemberGenerator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Entity: Member")
internal class MemberTest{

    @Test
    @DisplayName("회원 객체 생성")
    fun member(){
        // Given
        var name: String = "최용석"
        var email: String = "test@naver.com"
        var password: String = "password"
        var nickname: String = "김턱상"

        // When
        var member: Member = Member(0L, name, email, password, nickname)

        // Then
        assertEquals(member.id, 0L)
        assertEquals(member.name, name)
        assertEquals(member.email, email)
        assertEquals(member.password, password)
        assertEquals(member.nickname, nickname)
    }

    @Test
    @DisplayName("회원 정보 변경")
    fun updateName(){
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
        assertEquals(member.name, newName)
        assertEquals(member.email, newEmail)
        assertEquals(member.password, newPassword)
        assertEquals(member.nickname, newNickname)
    }
}