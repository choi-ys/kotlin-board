package io.example.board.repository

import io.example.board.domain.entity.redis.MailCache
import org.springframework.data.repository.CrudRepository

interface MailCacheRepo : CrudRepository<MailCache, String> {
}