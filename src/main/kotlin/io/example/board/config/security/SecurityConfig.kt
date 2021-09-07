package io.example.board.config.security

import io.example.board.config.security.jwt.JwtConfigurer
import io.example.board.config.security.jwt.TokenUtils
import org.springframework.http.HttpMethod.GET
import org.springframework.http.HttpMethod.POST
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.stereotype.Component

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Component
class SecurityConfig(
    private val tokenUtils: TokenUtils
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http
            .csrf().disable()
            .apply(JwtConfigurer(tokenUtils))
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            .and()
            .authorizeRequests {
                it
                    .antMatchers(GET, *SecurityRoles.NONE.patterns(GET)).permitAll()
                    .antMatchers(POST, *SecurityRoles.NONE.patterns(POST)).permitAll()
                    .anyRequest().authenticated()
            }
    }
}