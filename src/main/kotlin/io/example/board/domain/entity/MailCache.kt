package io.example.board.domain.entity

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

@RedisHash(
    value = "MAIL_CERTIFY_", // key prefix = @RedisHash Annotation value + Redis Entity @Id value
    timeToLive = 600L // 600L = 600s
)
data class MailCache(
    @Id
    var email: String,
    var certificationText: String,
)