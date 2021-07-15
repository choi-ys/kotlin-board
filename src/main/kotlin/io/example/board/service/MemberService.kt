package io.example.board.service

import io.example.board.domain.dto.request.SignupRequest
import io.example.board.domain.dto.response.SignupResponse
import io.example.board.domain.entity.Member
import io.example.board.repository.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.IllegalArgumentException

@Service
@Transactional(readOnly = true)
class MemberService(
   private val memberRepository: MemberRepository
) {

    /**
     * 회원 가입 요청
     * @return 가입 완료된 회원 정보
     * @apiNote 처리내용
     * <ul>
     *     <li>이메일 중복검사
     *     <li>TODO 비밀번호 암호화
     * </ul>
     */
    @Transactional
    fun signup(signupRequest: SignupRequest) : SignupResponse{
        if(memberRepository.existsByEmail(signupRequest.email)) {
            throw IllegalArgumentException("이미 존재하는 이메일 입니다.")
        }

        var member: Member = Member(
            0,
            signupRequest.name,
            signupRequest.email,
            signupRequest.password,
            signupRequest.nickname
        )

        val savedMember = memberRepository.save(member)

        return SignupResponse(
            savedMember.id,
            savedMember.name,
            savedMember.email,
            savedMember.nickname
        )
    }
}