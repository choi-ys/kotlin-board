package io.example.board.service

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import java.util.*

@Service
class MailService(
    private val javaMailSender: JavaMailSender
){

    private fun sendMail(simpleMailMessage: SimpleMailMessage){
        javaMailSender.send(simpleMailMessage)
    }

    // TODO MimeMessage(HTML)로 변경
    private fun createMailTemplate(
        to:String,
        title: String,
        content: String
    ) = SimpleMailMessage().also {
        it.setTo(to)
        it.setSubject(title)
        it.setText(content)
    }

    fun sendCertificationMail(email: String) : String{
        val title = "[kotlin-board] 회원 인증 메일"
        val content = UUID.randomUUID().toString()

        sendMail(createMailTemplate(email, title, content))
        return content
    }
}