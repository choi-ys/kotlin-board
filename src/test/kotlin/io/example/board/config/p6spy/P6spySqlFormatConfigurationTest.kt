package io.example.board.config.p6spy

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

/**
 * @author : choi-ys
 * @date : 2021-08-30 오전 4:19
 */
@DisplayName("Config:P6spySqlFormatConfiguration")
internal class P6spySqlFormatConfigurationTest {

    @Test
    @DisplayName("[casting] Timestamp -> LocalDateTime")
    fun castingTimestampToLocalDateTime() {
        // Given
        val givenTimestamp = "1630264221961"

        // When
        val expected = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(givenTimestamp.toLong()),
            TimeZone.getDefault().toZoneId()
        )

        // Then
        Assertions.assertTrue(expected is LocalDateTime)
    }

    @Test
    @DisplayName("[casting] LocalDateTime <- Timestamp")
    fun castingLocalDateTimeToTimestamp() {
        // Given
        val givenLocalDateTime = LocalDateTime.now()

        // When
        val expected = Timestamp.valueOf(givenLocalDateTime)

        // Then
        Assertions.assertTrue(expected is Timestamp)
    }
}