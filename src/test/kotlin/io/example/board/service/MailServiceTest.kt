package io.example.board.service

import io.example.board.config.test.MockingTestAnnotations
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender

@DisplayName("Service:Mail")
internal class MailServiceTest : MockingTestAnnotations(){

    @Mock
    lateinit var javaMailSender: JavaMailSender

    @InjectMocks
    lateinit var mailService: MailService

    @Test
    @DisplayName("회원 인증 메일 전송")
    fun send(){
        // Given
        var to = "rcn115@naver.com"
        val simpleMailMessage = SimpleMailMessage().also {
            it.setTo(to)
            it.setSubject("test title")
            it.setText("test content")
        }

//        doAnswer(`when`(javaMailSender.send(simpleMailMessage))).
            doNothing().`when`(javaMailSender).send(any(SimpleMailMessage::class.java))
//            doNothing().`when`(javaMailSender)

        // When
        val sendCertificationMail = mailService.sendCertificationMail(to)

        // Then
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage::class.java))
    }
}