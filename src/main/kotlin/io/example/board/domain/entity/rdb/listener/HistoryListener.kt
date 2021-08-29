package io.example.board.domain.entity.rdb.listener

/**
 * @author : choi-ys
 * @date : 2021-08-29 오후 11:50
 */
interface HistoryListener {
    fun postPersist(entity:Any)
    fun postUpdate(entity:Any)
    fun saveHistory(entity:Any, historyEventType: HistoryEventType)
}