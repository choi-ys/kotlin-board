package io.example.board.config.security

import io.example.board.config.security.jwt.verifier.JwtConfigurer
import io.example.board.config.security.jwt.verifier.TokenVerifier
import io.example.board.domain.entity.rdb.member.MemberRole
import org.springframework.http.HttpMethod.GET
import org.springframework.http.HttpMethod.POST
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.stereotype.Component

@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Component
class SecurityConfig(
    private val tokenVerifier: TokenVerifier,
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http
            .httpBasic().disable()
            .csrf().disable()
            .apply(JwtConfigurer(tokenVerifier))
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            .and()
            //TODO 인증된 토큰을 포함한 요청이지만 SecurityRoles에 명시되지 않은 endpoint의 요청이 정상적으로 처리되는 현상 수정
            .authorizeRequests {
                it
                    .antMatchers(GET, *SecurityRoles.NONE.patterns(GET)).permitAll()
                    .antMatchers(POST, *SecurityRoles.NONE.patterns(POST)).permitAll()
                    .antMatchers(GET, *SecurityRoles.MEMBER.patterns(GET)).hasRole(MemberRole.MEMBER.name)
                    .antMatchers(POST, *SecurityRoles.MEMBER.patterns(POST)).hasRole(MemberRole.MEMBER.name)
                    .anyRequest().authenticated()
            }
            .apply(JwtConfigurer(tokenVerifier))
    }
}