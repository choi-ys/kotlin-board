package io.example.board.config.security.jwt.certification

import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

class JwtConfigurer(
    private val tokenUtils: TokenUtils
) : SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>() {

    override fun configure(http: HttpSecurity?) {
        http?.addFilterAt(
            JwtFilter(tokenUtils),
            BasicAuthenticationFilter::class.java
        )
    }
}
