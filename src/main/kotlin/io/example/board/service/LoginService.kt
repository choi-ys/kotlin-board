package io.example.board.service

import io.example.board.config.security.jwt.verifier.TokenVerifier
import io.example.board.config.security.jwt.provider.TokenProvider
import io.example.board.domain.dto.request.LoginRequest
import io.example.board.domain.dto.request.RefreshTokenRequest
import io.example.board.domain.dto.response.LoginResponse
import io.example.board.domain.entity.rdb.member.Member
import io.example.board.domain.vo.login.LoginUserAdapter
import io.example.board.domain.vo.login.token.Token
import io.example.board.repository.MemberRepo
import mu.KotlinLogging
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private val logger = KotlinLogging.logger { }

@Service
@Transactional(readOnly = true)
class LoginService(
    private val passwordEncoder: PasswordEncoder,
    private val memberRepo: MemberRepo,
    private val tokenProvider: TokenProvider,
    private val tokenVerifier: TokenVerifier
) : UserDetailsService {

    fun login(loginRequest: LoginRequest): LoginResponse {
        val member = memberRepo.findByEmail(loginRequest.email).orElseThrow() {
            logger.error("요청에 해당하는 사용자가 없습니다.")
            throw UsernameNotFoundException("요청에 해당하는 사용자가 없습니다.")
        }

        if (!passwordEncoder.matches(loginRequest.password, member.password)) {
            logger.error("로그인에 실패하였습니다. 로그인 정보를 다시 확인해 주세요.")
            throw SecurityException("로그인에 실패하였습니다. 로그인 정보를 다시 확인해 주세요.")
        }
        return LoginResponse(member.id, member.email, member.nickname, issued(member))
    }

    override fun loadUserByUsername(username: String): UserDetails {
        val member = memberRepo.findByEmail(username).orElseThrow() {
            logger.error("요청에 해당하는 사용자가 없습니다.")
            throw UsernameNotFoundException("요청에 해당하는 사용자가 없습니다.")
        }
        return LoginUserAdapter(member.email, member.mapToSimpleGrantedAuthority())
    }

    fun issued(member: Member): Token {
        val loginUserAdapter = LoginUserAdapter(member.email, member.mapToSimpleGrantedAuthority())
        return tokenProvider.createToken(loginUserAdapter)
    }

    fun refresh(refreshTokenRequest: RefreshTokenRequest): Token {
        val verify = tokenVerifier.verify(refreshTokenRequest.refreshToken)
        val userDetails = loadUserByUsername(verify.username)
        return tokenProvider.createToken(userDetails)
    }
}