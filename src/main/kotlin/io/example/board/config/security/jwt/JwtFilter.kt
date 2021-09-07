package io.example.board.config.security.jwt

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
        if("" != token){
            val verifyResult = tokenUtils.verify(token)
            if (verifyResult.success) {
                SecurityContextHolder.getContext().authentication = token?.let {
                    tokenUtils.getAuthentication(it)
                }
            }
        }

        chain.doFilter(request, response)
        return
    }
}
