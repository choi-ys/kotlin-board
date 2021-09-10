package io.example.board.util.generator

import io.example.board.domain.dto.request.SignupRequest
import io.example.board.domain.entity.rdb.member.Member
import io.example.board.repository.MemberRepository
import org.junit.jupiter.api.Disabled
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestComponent

@Disabled
@TestComponent
class MemberGenerator {

    @Autowired
    lateinit var memberRepository: MemberRepository

    fun savedMember(): Member {
        return memberRepository.saveAndFlush(MemberGenerator.generateMemberEntity())
    }

    companion object {
        var name: String = "최용석"
        var email: String = "test@naver.com"
        var password: String = "password"
        var nickname: String = "김턱상"

        fun generateMemberEntity() = Member(name, email, password, nickname)

        fun generateSignupRequest() = SignupRequest(name, email, password, nickname)
    }
}