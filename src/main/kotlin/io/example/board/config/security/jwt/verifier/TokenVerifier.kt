package io.example.board.config.security.jwt.verifier

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.example.board.config.security.jwt.verifier.VerifyResult.Companion.mapFor
import io.example.board.domain.vo.login.LoginUserAdapter
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import javax.servlet.http.HttpServletRequest

@Component
class TokenVerifier : InitializingBean {
    @Value("\${jwt.signature}")
    private val SIGNATURE: String? = null
    private var ALGORITHM: Algorithm? = null

    /**
     * TokenUtils Bean 생성 이후 application.yml의 jwt.signature_key값 로드 완료 후 ALGORITHM 초기화
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

    // TODO JWTDecodeException, SignatureVerificationException, TokenExpiredException 예외 처리
    fun verify(token: String): VerifyResult {
        val verify = JWT.require(ALGORITHM).build().verify(token)
        val claims = verify.claims
        return mapFor(true, claims)

//        return try {
//            val verify = JWT.require(ALGORITHM).build().verify(token)
//            val claims = verify.claims
//            mapFor(true, claims)
//        } catch (ex: Exception) {
//            val decode = JWT.decode(token)
//            val claims = decode.claims
//            mapFor(false, claims)
//        }
    }

    fun getAuthentication(token: String): Authentication {
        val verifyResult = verify(token)
        val loginUserAdapter = LoginUserAdapter(verifyResult.username, verifyResult.authorities)
        return UsernamePasswordAuthenticationToken(loginUserAdapter, null, loginUserAdapter.authorities)
    }
}