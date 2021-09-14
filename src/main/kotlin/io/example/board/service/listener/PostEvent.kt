package io.example.board.service.listener

import io.example.board.domain.dto.response.PostResponse
import java.util.*
import javax.swing.event.DocumentEvent

/**
 * @author : choi-ys
 * @date : 2021/09/13 1:23 오후
 */
data class PostEvent(
    val eventId: String = UUID.randomUUID().toString(),
    val postResponse: PostResponse,
    val type: DocumentEvent.EventType
)
