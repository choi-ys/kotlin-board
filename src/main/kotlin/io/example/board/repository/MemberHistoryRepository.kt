package io.example.board.repository

import io.example.board.domain.entity.rdb.history.MemberHistory
import org.springframework.data.jpa.repository.JpaRepository

/**
 * @author : choi-ys
 * @date : 2021-08-30 오전 12:42
 */
interface MemberHistoryRepository : JpaRepository<MemberHistory, Long> {
}