package io.example.board.config.security.jwt.certification

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

@Component
class JwtFilter(
    private val tokenUtils: TokenUtils
) : GenericFilterBean() {

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val token = tokenUtils.resolve(request as HttpServletRequest)
        if ("" != token) {
            val verifyResult = tokenUtils.verify(token)
            if (verifyResult.success) {
                val authentication = tokenUtils.getAuthentication(token)
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
