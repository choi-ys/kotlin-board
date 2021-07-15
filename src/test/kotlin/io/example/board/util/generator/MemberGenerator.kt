package io.example.board.util.generator

import io.example.board.domain.dto.request.SignupRequest
import io.example.board.domain.entity.Member
import io.example.board.repository.MemberRepository
import org.junit.jupiter.api.Disabled
import org.springframework.beans.factory.annotation.Autowired

@Disabled
class MemberGenerator {

    companion object {
        var name: String = "최용석"
        var email: String = "test@naver.com"
        var password: String = "password"
        var nickname: String = "김턱상"

        fun generateMemberEntity() = Member(0L, name, email, password, nickname)

        fun generateSignupRequest() = SignupRequest(name, email, password, nickname)
    }

    @Autowired
    lateinit var memberRepository: MemberRepository

    fun savedMemberEntity() : Member {
        return memberRepository.save(generateMemberEntity())
    }
}