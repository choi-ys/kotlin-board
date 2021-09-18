package io.example.board.config.security.jwt

import io.example.board.config.security.jwt.certification.TokenUtils
import io.example.board.config.security.jwt.offer.TokenProvider
import io.example.board.domain.entity.rdb.member.MemberRole
import io.example.board.util.LocalDateTimeUtils
import io.example.board.util.generator.MemberGenerator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.userdetails.User
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * @author : choi-ys
 * @date : 2021/09/09 12:13 오후
 */
@SpringBootTest(classes = [TokenProvider::class, TokenUtils::class])
@DisplayName("Config:TokenProvier")
internal class TokenProviderTest {

    @Autowired
    lateinit var tokenProvider: TokenProvider

    @Autowired
    lateinit var tokenUtils: TokenUtils

    @Test
    @DisplayName("토큰 생성")
    fun createToken() {
        // Given
        val member = MemberGenerator.member()
        member.addRoles(setOf(MemberRole.ADMIN, MemberRole.SYSTEM_ADMIN))
        val user = User(member.email, "", member.mapToSimpleGrantedAuthority())

        // When
        val createdToken = tokenProvider.createToken(user)

        // Then
        val accessTokenVerifyResult = tokenUtils.verify(createdToken.accessToken)
        val refreshTokenVerifyResult = tokenUtils.verify(createdToken.refreshToken)

        val accessExpiredLocalDateTime = LocalDateTimeUtils.timestampToLocalDateTime(createdToken.accessExpired.time)
        val refreshExpiredLocalDateTime = LocalDateTimeUtils.timestampToLocalDateTime(createdToken.refreshExpired.time)
        val ofPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

        assertAll(
            { assertEquals(accessTokenVerifyResult.username, member.email) },
            { assertEquals(refreshTokenVerifyResult.username, accessTokenVerifyResult.username) },
            { assertTrue(accessTokenVerifyResult.authorities!!.containsAll(member.mapToSimpleGrantedAuthority())) },
            { assertTrue(refreshTokenVerifyResult.authorities!!.containsAll(member.mapToSimpleGrantedAuthority())) },
            {
                assertEquals(
                    LocalDateTime.now().plusMinutes(10).format(ofPattern),
                    accessExpiredLocalDateTime.format(ofPattern),
                    "발급된 접근 토큰의 유효기간이 10분인지 확인"
                )
            },
            {
                assertEquals(
                    LocalDateTime.now().plusMinutes(20).format(ofPattern),
                    refreshExpiredLocalDateTime.format(ofPattern),
                    "발급된 갱신 토큰의 유효기간이 20분인지 확인"
                )
            }
        )
    }
}