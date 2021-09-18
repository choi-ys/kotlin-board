package io.example.board.config.security.jwt.certification

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.SignatureVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import io.example.board.domain.entity.rdb.member.MemberRole
import io.example.board.domain.vo.login.LoginUserAdapter
import io.example.board.util.generator.TokenGenerator
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * @author : choi-ys
 * @date : 2021/09/13 6:22 오후
 */
@SpringBootTest
@Import(TokenGenerator::class)
@Transactional
@DisplayName("Config:TokenUtils")
internal class TokenUtilsTest {

    @Autowired
    lateinit var tokenGenerator: TokenGenerator

    @Autowired
    lateinit var tokenUtils: TokenUtils

    @Test
    @DisplayName("정상 JWT 검증")
    fun verify() {
        // Given
        val generateToken = tokenGenerator.generateToken()

        // When
        val accessTokenVerifyResult = tokenUtils.verify(generateToken.accessToken)
        val refreshTokenVerifyResult = tokenUtils.verify(generateToken.refreshToken)

        assertAll(
            { assertTrue(accessTokenVerifyResult.success) },
            { assertTrue(refreshTokenVerifyResult.success) }
        )
    }

    @Test
    @DisplayName("JWT 형식이 아닌 토큰 검증")
    fun verityInvalidJwtFormat() {
        // Given
        val invalidJsonFormat = "test"

        // When & Then
        assertThrows(JWTDecodeException::class.java) {
            tokenUtils.verify(invalidJsonFormat)
        }.let {
            assertEquals(it.javaClass.simpleName, JWTDecodeException::class.java.simpleName)
            assertEquals(it.message, "The token was expected to have 3 parts, but got 1.")
        }
    }

    @Test
    @DisplayName("JSON 형식이 아닌 토큰 검증")
    fun verityInvalidJsonFormat() {
        // Given
        val inValidJsonFormat = "header.payload.signature"

        // When & Then
        assertThrows(JWTDecodeException::class.java) {
            tokenUtils.verify(inValidJsonFormat)
        }.let {
            assertEquals(it.javaClass.simpleName, JWTDecodeException::class.java.simpleName)
            assertTrue(it.message?.contains("doesn't have a valid JSON format") ?: false)
        }
    }

    @Test
    @DisplayName("잘못된 서명을 가진 토큰 검증")
    fun verifyUnsupportedJwt() {
        // Given
        val currentTimeMillis = System.currentTimeMillis()
        val ACCESS_TOKEN_VALIDITY_IN_SECONDS_TERM = 1000L
        val SIGNATURE = "test"

        val loginUserAdapter =
            LoginUserAdapter("test@naver.com", setOf(SimpleGrantedAuthority("ROLE_" + MemberRole.MEMBER)))

        val jwt = JWT.create()
            .withIssuer("ISSUER")
            .withSubject("SUBJECT")
            .withAudience("AUDIENCE")
            .withIssuedAt(Date(currentTimeMillis))
            .withExpiresAt(Date(currentTimeMillis + ACCESS_TOKEN_VALIDITY_IN_SECONDS_TERM * 1000))
            .withClaim(ClaimKey.USE.value, TokenType.ACCESS.name)
            .withClaim(ClaimKey.USERNAME.value, loginUserAdapter.username)
            .withClaim(ClaimKey.AUTHORITIES.value, loginUserAdapter.authorities.joinToString(","))
            .sign(Algorithm.HMAC256(SIGNATURE))

        // When & Then
        assertThrows(SignatureVerificationException::class.java) {
            tokenUtils.verify(jwt)
        }.let {
            assertEquals(it::class.java.simpleName, SignatureVerificationException::class.java.simpleName)
            assertTrue(
                it.message?.contains("The Token's Signature resulted invalid when verified using the Algorithm")
                    ?: false,
            )
        }
    }

    @Test
    @DisplayName("유효기간이 만료된 토큰 검증")
    fun verifyExpiredJwt() {
        // Given
        val currentTimeMillis = System.currentTimeMillis()
        val ACCESS_TOKEN_VALIDITY_IN_SECONDS_TERM = 1L
        val SIGNATURE = "test-signature-key"

        val loginUserAdapter =
            LoginUserAdapter("test@naver.com", setOf(SimpleGrantedAuthority("ROLE_" + MemberRole.MEMBER)))

        val jwt = JWT.create()
            .withIssuer("ISSUER")
            .withSubject("SUBJECT")
            .withAudience("AUDIENCE")
            .withIssuedAt(Date(currentTimeMillis))
            .withExpiresAt(Date(currentTimeMillis + ACCESS_TOKEN_VALIDITY_IN_SECONDS_TERM))
            .withClaim(ClaimKey.USE.value, TokenType.ACCESS.name)
            .withClaim(ClaimKey.USERNAME.value, loginUserAdapter.username)
            .withClaim(ClaimKey.AUTHORITIES.value, loginUserAdapter.authorities.joinToString(","))
            .sign(Algorithm.HMAC256(SIGNATURE))

        // When
        Thread.sleep(1000L)
        assertThrows(TokenExpiredException::class.java) {
            tokenUtils.verify(jwt)
        }.let {
            assertEquals(it.javaClass.simpleName, TokenExpiredException::class.java.simpleName)
            assertTrue(it.message?.contains("The Token has expired") ?: false)
        }
    }
}
