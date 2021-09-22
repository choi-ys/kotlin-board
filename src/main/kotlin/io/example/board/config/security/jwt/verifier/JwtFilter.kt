package io.example.board.config.security.jwt.verifier

import io.example.board.config.security.jwt.verifier.TokenVerifier
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

class JwtFilter(
    private val tokenVerifier: TokenVerifier,
) : GenericFilterBean() {

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val token = tokenVerifier.resolve(request as HttpServletRequest)
        if ("" != token) {
            val verifyResult = tokenVerifier.verify(token)
            if (verifyResult?.success ?: false) {
                val authentication = tokenVerifier.getAuthentication(token)
                SecurityContextHolder.getContext().authentication = authentication
            }
        } else {
            // TODO HeaderWriterFilter, SecurityContextHolderAwareRequestFilter에서 해당 filter가 모두 동작 되는 현상 수정
            logger.debug("HeaderWriterFilter, SecurityContextHolderAwareRequestFilter에서 해당 filter가 모두 동작 되는 현상")
        }

        chain.doFilter(request, response)
        return
    }
}
