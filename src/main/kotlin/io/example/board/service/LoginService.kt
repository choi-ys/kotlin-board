package io.example.board.service

import io.example.board.domain.dto.request.LoginRequest
import io.example.board.domain.dto.response.LoginResponse
import io.example.board.repository.MemberRepository
import mu.KotlinLogging
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private val logger = KotlinLogging.logger { }

@Service
@Transactional(readOnly = true)
class LoginService(
    private val passwordEncoder: PasswordEncoder,
    private val memberRepository: MemberRepository
) {

    fun login(loginRequest: LoginRequest): LoginResponse {
        val member = memberRepository.findByEmail(loginRequest.email).orElseThrow() {
            logger.error("요청에 해당하는 사용자가 없습니다.")
            throw UsernameNotFoundException("요청에 해당하는 사용자가 없습니다.")
        }

        if (!passwordEncoder.matches(loginRequest.password, member.password)) {
            logger.error("로그인에 실패하였습니다. 로그인 정보를 다시 확인해 주세요.")
            throw SecurityException("로그인에 실패하였습니다. 로그인 정보를 다시 확인해 주세요.")
        }
        return LoginResponse(member.id, member.email, member.nickname)
    }
}