package io.example.board.repository

import io.example.board.config.redis.EmbeddedRedisConfig
import io.example.board.config.test.BaseTestConfig
import io.example.board.domain.entity.redis.MailCache
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest
import org.springframework.context.annotation.Import
import java.util.*

@Import(EmbeddedRedisConfig::class)
@DataRedisTest
@Disabled
@DisplayName("Repo:MailCache")
internal class MailCacheRepoTest : BaseTestConfig() {

    @Autowired
    lateinit var mailCacheRepo: MailCacheRepo

    @Value("\${spring.redis.host}")
    private val redisHost = ""

    @Value("\${spring.redis.port}")
    private val redisPort = 0

    @Test
    @DisplayName("인증 메일 캐시 저장")
    fun saveRedis() {
        // Given
        var mailCache = generateMailCache()

        // When
        val savedMailCache = mailCacheRepo.save(mailCache)

        // Then
        var selectedMailCache = mailCacheRepo.findById(savedMailCache.email).orElseThrow()
        assertEquals(savedMailCache.email, selectedMailCache.email)
        assertEquals(savedMailCache.certificationText, selectedMailCache.certificationText)
    }

    private fun generateMailCache(): MailCache {
        var mail = "rcn115@naver.com"
        var certificationText = UUID.randomUUID().toString()
        return MailCache(mail, certificationText)
    }
}