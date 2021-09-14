package io.example.board.service.listener

import mu.KotlinLogging
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

/**
 * @author : choi-ys
 * @date : 2021/09/13 12:52 오후
 */

private val logger = KotlinLogging.logger {}

@Component
class PostEventListener {

    @EventListener
    fun onPostEventHandler(postEvent: PostEvent) {
        logger.info("[EVENT] : {}", postEvent)
    }
}