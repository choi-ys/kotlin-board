package io.example.board.util.generator

import io.example.board.domain.dto.request.LoginRequest
import io.example.board.domain.vo.login.token.Token
import io.example.board.service.LoginService
import io.example.board.service.MemberService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author : choi-ys
 * @date : 2021/09/10 2:38 오후
 */
@Component
class TokenGenerator {

    @Autowired
    private lateinit var memberService: MemberService

    @Autowired
    private lateinit var loginService: LoginService

    fun generateToken(): Token {
        val signupRequest = MemberGenerator.generateSignupRequest()
        memberService.signup(signupRequest)

        val loginRequest = LoginRequest(signupRequest.email, MemberGenerator.password)

        return loginService.login(loginRequest).token
    }

    companion object {
        fun makeBearerToken(token: String): String {
            return "Bearer $token"
        }
    }
}