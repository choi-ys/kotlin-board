package io.example.board.domain.entity.redis

import io.example.board.domain.vo.login.token.Token
import org.springframework.data.redis.core.RedisHash
import javax.persistence.Id

/**
 * @author : choi-ys
 * @date : 2021/09/08 4:30 오후
 */
@RedisHash(
    value = "BLACK_LIST_", // key prefix = @RedisHash Annotation value + Redis Entity @Id value
    timeToLive = 600L // 600L = 600s
)
data class BlackListTokenCache(
    @Id
    val principal: String,

    val token: Token
)