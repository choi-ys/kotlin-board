package io.example.board.util.generator

import io.example.board.config.security.jwt.offer.TokenProvider
import io.example.board.domain.entity.rdb.member.Member
import io.example.board.domain.vo.login.LoginUserAdapter
import io.example.board.domain.vo.login.token.Token
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestComponent
import org.springframework.context.annotation.Import

/**
 * @author : choi-ys
 * @date : 2021/09/10 2:38 오후
 */
@TestComponent
@Import(MemberGenerator::class)
class TokenGenerator {

    @Autowired
    private lateinit var memberGenerator: MemberGenerator

    @Autowired
    private lateinit var tokenProvider: TokenProvider

    fun generateToken(): Token {
        val savedMember = memberGenerator.savedMember()
        val loginUserAdapter = LoginUserAdapter(savedMember.email, savedMember.mapToSimpleGrantedAuthority())
        return tokenProvider.createToken(loginUserAdapter)
    }

    fun generateToken(savedMember: Member): Token {
        val loginUserAdapter = LoginUserAdapter(savedMember.email, savedMember.mapToSimpleGrantedAuthority())
        return tokenProvider.createToken(loginUserAdapter)
    }

    companion object {
        fun makeBearerToken(token: String): String {
            return "Bearer $token"
        }
    }
}