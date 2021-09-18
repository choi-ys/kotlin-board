package io.example.board.util.generator

import io.example.board.domain.dto.request.SignupRequest
import io.example.board.domain.entity.rdb.member.Member
import io.example.board.domain.vo.login.LoginUser
import io.example.board.repository.MemberRepo
import org.junit.jupiter.api.Disabled
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestComponent

@Disabled
@TestComponent
class MemberGenerator {

    @Autowired
    lateinit var memberRepo: MemberRepo

    fun savedMember(): Member {
        return memberRepo.saveAndFlush(member())
    }

    companion object {
        var name = "최용석"
        var email = "test@naver.com"
        var password = "password"
        var nickname = "김턱상"

        fun member() = Member(name, email, password, nickname)

        fun signupRequest() = SignupRequest(name, email, password, nickname)

        fun loginUser() = LoginUser(email, member().mapToSimpleGrantedAuthority())
    }
}