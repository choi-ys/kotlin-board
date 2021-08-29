package io.example.board.config.p6spy

import com.p6spy.engine.common.P6Util
import com.p6spy.engine.logging.Category
import com.p6spy.engine.spy.appender.MessageFormattingStrategy
import org.hibernate.engine.jdbc.internal.FormatStyle
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

/**
 * @author : choi-ys
 * @date : 2021/04/13 5:52 오후
 * @Content : 실행 쿼리 출력 시 pretty print를 위한 MessageFormatter
 */
class P6spySqlFormatConfiguration : MessageFormattingStrategy {
    override fun formatMessage(
        connectionId: Int,
        now: String,
        elapsed: Long,
        category: String,
        prepared: String,
        sql: String,
        url: String
    ): String {
        var sql: String? = sql
        sql = formatSql(category, sql)
        return "\n -> [Meta info] : " +
                LocalDateTime.ofInstant(Instant.ofEpochMilli(now.toLong()), TimeZone.getDefault().toZoneId()) +
                " | " + elapsed + "ms | connection " + connectionId +
                " \n -> [Prepared statement]: " + P6Util.singleLine(prepared) + sql
    }

    private fun addSeparator(str: String){
    }

    private fun metaLogFormatter(vararg test: String): String {
        return ""
    }

    private fun formatSql(category: String, sql: String?): String? {
        var sql = sql
        if (sql == null || sql.trim { it <= ' ' } == "") return sql

        // Only format Statement, distinguish DDL And DML
        if (Category.STATEMENT.name == category) {
            val tmpsql = sql.trim { it <= ' ' }.toLowerCase(Locale.ROOT)
            sql = if (tmpsql.startsWith("create") || tmpsql.startsWith("alter") || tmpsql.startsWith("comment")) {
                FormatStyle.DDL.formatter.format(sql)
            } else {
                FormatStyle.BASIC.formatter.format(sql)
            }
            sql = "\n -> [Hibernate format]: $sql"
        }
        return sql
    }
}