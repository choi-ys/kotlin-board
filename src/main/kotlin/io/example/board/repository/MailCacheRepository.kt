package io.example.board.repository

import io.example.board.domain.entity.MailCache
import org.springframework.data.repository.CrudRepository

interface MailCacheRepository : CrudRepository<MailCache, String> {
}