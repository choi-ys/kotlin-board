package io.example.board.domain.entity.rdb.listener

import io.example.board.domain.entity.rdb.common.EventType

/**
 * @author : choi-ys
 * @date : 2021-08-29 오후 11:50
 */
// TODO 해당 이벤트 발생 시, Kafka를 이용한 Event 기반 아키텍쳐로 이력 관리
interface HistoryListener {
    fun postPersist(entity: Any)
    fun postUpdate(entity: Any)
    fun saveHistory(entity: Any, eventType: EventType)
}