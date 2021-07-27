package io.example.board.service

import io.example.board.advice.CommonException
import io.example.board.advice.Error
import io.example.board.domain.dto.request.MemberCertifyRequest
import io.example.board.domain.dto.request.SignupRequest
import io.example.board.domain.dto.response.SignupResponse
import io.example.board.domain.entity.rdb.member.Member
import io.example.board.domain.entity.rdb.member.MemberStatus
import io.example.board.domain.entity.redis.MailCache
import io.example.board.repository.MailCacheRepository
import io.example.board.repository.MemberRepository
import mu.KotlinLogging
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private val logger = KotlinLogging.logger {  }

@Service
@Transactional(readOnly = true)
class MemberService(
   private val memberRepository: MemberRepository,
   private val passwordEncoder: PasswordEncoder,
   private val mailService: MailService,
   private val mailCacheRepository: MailCacheRepository
) {

    /**
     * 회원 가입
     * @return 가입 완료된 회원 정보
     * @apiNote 처리내용
     * <ul>
     *     <li>이메일 중복검사
     *     <li>비밀번호 암호화
     * </ul>
     */
    @Transactional
    fun signup(signupRequest: SignupRequest) : SignupResponse{
        if(memberRepository.existsByEmail(signupRequest.email)) {
            val errorMessage = "이미 존재하는 이메일 입니다."
            throw CommonException(Error(signupRequest.email, errorMessage))
        }

        val memberValue = Member(
            name = signupRequest.name,
            email = signupRequest.email,
            password = passwordEncoder.encode(signupRequest.password),
            nickname = signupRequest.nickname
        )

        var savedMember = memberRepository.save(memberValue)

        return SignupResponse(
            id = savedMember.id,
            name = savedMember.name,
            email = savedMember.email,
            nickname = savedMember.nickname
        )
    }

    fun receiptCertify(id: Long) : MailCache {
        val member = memberRepository.findById(id).orElseThrow() {
            val errorMessage = "요청에 해당하는 사용자가 없습니다."
            throw CommonException(Error(id, errorMessage))
        }
        val certificationText = mailService.sendCertificationMail(member.email)
        return mailCacheRepository.save(MailCache(member.email, certificationText))
    }

    fun checkCertify(memberCertifyRequest: MemberCertifyRequest) : Boolean{
        val member = memberRepository.findById(memberCertifyRequest.id).orElseThrow(){
            val errorMessage = "요청에 해당하는 사용자가 없습니다."
            throw CommonException(Error(memberCertifyRequest.id, errorMessage))
        }
        val certifyMail = mailCacheRepository.findById(memberCertifyRequest.email).orElseThrow(){
            val errorMessage = "인증시간이 만료되었거나, 잘못된 요청입니다. 다시 시도해주세요."
            throw CommonException(Error(memberCertifyRequest.email, errorMessage))
        }
        if(memberCertifyRequest.certificationTest != certifyMail.certificationText) return false

        member.status = MemberStatus.CERTIFIED
        return true
    }
}