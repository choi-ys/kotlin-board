package io.example.board.config.security.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.example.board.domain.vo.login.token.Token
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*

@Component
class TokenProvider : InitializingBean {
    @Value("\${jwt.signature}")
    private val SIGNATURE: String? = null
    private var ALGORITHM: Algorithm? = null

    @Value("\${jwt.issuer}")
    private val ISSUER: String? = null

    @Value("\${jwt.subject}")
    private val SUBJECT: String? = null

    @Value("\${jwt.audience}")
    private val AUDIENCE: String? = null

    @Value("\${jwt.access-token-validity-in-seconds-term}")
    private val ACCESS_TOKEN_VALIDITY_IN_SECONDS_TERM: Long? = null

    @Value("\${jwt.refresh-token-validity-in-seconds-term}")
    private val REFRESH_TOKEN_VALIDITY_IN_SECONDS_TERM: Long? = null

    /**
     * TokenProvider Bean 생성 이후 application.yml의 jwt.signature_key값 로드 완료 후 ALGORITHM 초기화
     */
    override fun afterPropertiesSet() {
        ALGORITHM = Algorithm.HMAC256(SIGNATURE)
    }

    fun createToken(userDetails: UserDetails): Token {
        val currentTimeMillis = System.currentTimeMillis()
        val accessExpired = Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_IN_SECONDS_TERM!! * 1000)
        val refreshExpired = Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY_IN_SECONDS_TERM!! * 1000)
        val accessToken = accessTokenBuilder(currentTimeMillis, userDetails)
        val refreshToken = refreshTokenBuilder(currentTimeMillis, userDetails)
        return Token(accessToken, refreshToken, accessExpired, refreshExpired)
    }

    private fun accessTokenBuilder(currentTimeMillis: Long, userDetails: UserDetails): String {
        return JWT.create()
            .withIssuer(ISSUER)
            .withSubject(SUBJECT)
            .withAudience(AUDIENCE)
            .withIssuedAt(Date(currentTimeMillis))
            .withExpiresAt(Date(currentTimeMillis + ACCESS_TOKEN_VALIDITY_IN_SECONDS_TERM!!))
            .withClaim(ClaimKey.USE.value, TokenType.ACCESS.name)
            .withClaim(ClaimKey.USERNAME.value, userDetails.username)
            .withClaim(ClaimKey.AUTHORITIES.value, userDetails.authorities.joinToString(","))
            .sign(Algorithm.HMAC256(SIGNATURE))
    }

    private fun refreshTokenBuilder(currentTimeMillis: Long, userDetails: UserDetails): String {
        return JWT.create()
            .withIssuer(ISSUER)
            .withSubject(SUBJECT)
            .withAudience(AUDIENCE)
            .withIssuedAt(Date(currentTimeMillis))
            .withExpiresAt(Date(currentTimeMillis + REFRESH_TOKEN_VALIDITY_IN_SECONDS_TERM!!))
            .withClaim(ClaimKey.USE.value, TokenType.REFRESH.name)
            .withClaim(ClaimKey.USERNAME.value, userDetails.username)
            .sign(Algorithm.HMAC256(SIGNATURE))
    }
}