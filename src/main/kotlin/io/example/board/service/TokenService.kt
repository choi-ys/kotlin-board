package io.example.board.service

import io.example.board.config.security.jwt.offer.TokenProvider
import io.example.board.domain.entity.rdb.member.Member
import io.example.board.domain.vo.login.LoginUserAdapter
import io.example.board.domain.vo.login.token.Token
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author : choi-ys
 * @date : 2021/09/10 12:58 오전
 */
@Service
@Transactional(readOnly = true)
class TokenService(
    private val tokenProvider: TokenProvider
) {

    fun issued(member: Member): Token {
        val loginUserAdapter = LoginUserAdapter(member.email, member.mapToSimpleGrantedAuthority())
        return tokenProvider.createToken(loginUserAdapter)
    }
}