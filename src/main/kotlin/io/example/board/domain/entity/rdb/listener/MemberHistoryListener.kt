package io.example.board.domain.entity.rdb.listener

import io.example.board.domain.entity.rdb.common.EventType
import io.example.board.domain.entity.rdb.history.MemberHistory
import io.example.board.domain.entity.rdb.member.Member
import io.example.board.repository.MemberHistoryRepository
import io.example.board.util.ApplicationContextUtil
import mu.KotlinLogging
import javax.persistence.PostPersist
import javax.persistence.PostUpdate

/**
 * @author : choi-ys
 * @date : 2021-08-30 오전 12:37
 */
private val logger = KotlinLogging.logger { }

class MemberHistoryListener : HistoryListener {

    @PostPersist
    override fun postPersist(entity: Any) {
        saveHistory(entity, EventType.PERSIST)
    }

    @PostUpdate
    override fun postUpdate(entity: Any) {
        saveHistory(entity, EventType.UPDATE)
    }

    override fun saveHistory(entity: Any, historyEventType: EventType) {
        ApplicationContextUtil.getBean(MemberHistoryRepository::class.java)
            .save(MemberHistory.mapFor(entity as Member, historyEventType))
        logger.info("[History:Member][Event:{}]", historyEventType)
    }
}