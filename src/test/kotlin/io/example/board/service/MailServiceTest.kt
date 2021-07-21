package io.example.board.service

import io.example.board.config.base.BaseTestAnnotations
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@DisplayName("Service:Mail")
internal class MailServiceTest : BaseTestAnnotations(){

    @Autowired
    lateinit var mailService: MailService

    @Test
    @DisplayName("회원 인증 메일 전송")
    @Disabled
    fun send(){
        var to = "rcn115@naver.com"
        mailService.sendCertificationMail(to)
    }
}