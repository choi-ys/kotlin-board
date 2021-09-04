package io.example.board.config.test.assertions

import io.example.board.domain.entity.rdb.common.Auditor
import io.example.board.domain.entity.rdb.member.Member
import org.junit.jupiter.api.Assertions.*
import org.springframework.util.Assert

/**
 * @author : choi-ys
 * @date : 2021-08-23 오후 10:37
 */
interface ObjectAssertions {
    companion object {
        private fun <T : Auditor> assertAuditor(entity: T) {
            Assert.notNull(entity, "Entity must not be null")
            assertAll(
                { assertNotNull(entity.createdAt, MUST_EXIST_AUDITOR) },
                { assertNotNull(entity.createdBy, MUST_EXIST_AUDITOR) },
                { assertNotNull(entity.updatedAt, MUST_EXIST_AUDITOR) },
                { assertNotNull(entity.updatedBy, MUST_EXIST_AUDITOR) },
            )
        }

        fun <T : Member> assertMember(entity: T, given: Member) {
            assertAuditor(entity)
            assertAll(
                { assertEquals(entity.id, given.id, MUST_EXIST_PK) },
                { assertEquals(entity.email, given.email, NOT_NULL_STRING_PROPERTY) },
                { assertEquals(entity.name, given.name, NOT_NULL_STRING_PROPERTY) },
                { assertEquals(entity.nickname, given.nickname, NOT_NULL_STRING_PROPERTY) },
                { assertEquals(entity.status, given.status, MUST_EXIST) },
            )
        }

        private const val MUST_EXIST_PK = "PK는 반드시 존재해야 한다."
        private const val MUST_EXIST_AUDITOR = "생성/수정 관련 Entity metadata는 반드시 존재해야 한다. "
        private const val MUST_EXIST = "해당 항목은 Null이 아닌 값이 반드시 존재해야 한다."
        private const val NOT_NULL_STRING_PROPERTY = "해당 항목은 Null, 빈값, 공백이 아닌 값이 존재해야 한다."
    }
}