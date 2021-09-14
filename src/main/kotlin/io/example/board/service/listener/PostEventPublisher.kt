package io.example.board.service.listener

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

/**
 * @author : choi-ys
 * @date : 2021/09/13 12:52 오후
 */
@Component
class PostEventPublisher(
    private val applicationEventPublisher: ApplicationEventPublisher
) {
    fun notifyPosting(postEvent: PostEvent){
        applicationEventPublisher.publishEvent(postEvent)
    }
}