package io.example.board.config.security.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.example.board.config.security.jwt.VerifyResult.Companion.mapFor
import io.example.board.service.LoginService
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import javax.servlet.http.HttpServletRequest

@Component
class TokenUtils(
    val loginService: LoginService
) : InitializingBean {
    @Value("\${jwt.signature}")
    private val SIGNATURE: String? = null
    private var ALGORITHM: Algorithm? = null

    /**
     * TokenProvider Bean 생성 이후 application.yml의 jwt.signature_key값 로드 완료 후 ALGORITHM 초기화
     */
    override fun afterPropertiesSet() {
        ALGORITHM = Algorithm.HMAC256(SIGNATURE)
    }

    fun resolve(httpServletRequest: HttpServletRequest): String {
        val bearerToken = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION)
        return if (!StringUtils.hasText(bearerToken)) {
            ""
        } else {
            val isBearerToken = bearerToken.startsWith("Bearer ")
            if (isBearerToken) {
                bearerToken.substring(7, bearerToken.length)
            } else {
                ""
            }
        }
    }

    fun verify(token: String): VerifyResult {
        return try {
            val verify = JWT.require(ALGORITHM).build().verify(token)
            val claims = verify.claims
            mapFor(true, claims)
        } catch (ex: Exception) {
            val decode = JWT.decode(token)
            val claims = decode.claims
            mapFor(false, claims)
        }
    }

    fun getAuthentication(token: String): Authentication {
        val userDetails = loginService.loadUserByUsername(getUsername(token))
        return UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
    }

    fun getUsername(token: String?): String {
        return JWT.require(ALGORITHM).build().verify(token).getClaim(ClaimKey.PRINCIPAL.value).asString()
    }
}