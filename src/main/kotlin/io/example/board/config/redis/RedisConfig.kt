package io.example.board.config.redis

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories

private val logger = KotlinLogging.logger {  }

@Configuration
@EnableRedisRepositories
class RedisConfig {

    @Value("\${spring.redis.host}")
    private val redisHost = ""

    @Value("\${spring.redis.port}")
    private val redisPort = 0

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory? {
        logger.info("redisConnectionFactory is loaded by -> " + redisHost + " : " + redisPort)
        return LettuceConnectionFactory(redisHost, redisPort)
    }

    @Bean
    fun redisTemplate(): RedisTemplate<*, *>? {
        val redisTemplate = RedisTemplate<ByteArray, ByteArray>()
        redisTemplate.setConnectionFactory(redisConnectionFactory()!!)
        return redisTemplate
    }
}