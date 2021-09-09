package io.example.board.util

import java.time.Instant
import java.time.LocalDateTime
import java.util.*

/**
 * @author : choi-ys
 * @date : 2021/09/09 10:44 오후
 */
class LocalDateTimeUtils {
    companion object {

        fun timestampToLocalDateTime(timestamp: Long): LocalDateTime {
            return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(timestamp),
                TimeZone.getDefault().toZoneId()
            )
        }
    }
}