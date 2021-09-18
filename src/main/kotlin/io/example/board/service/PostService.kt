package io.example.board.service

import io.example.board.aspect.exception.CommonException
import io.example.board.domain.dto.request.PostRequest
import io.example.board.domain.dto.response.PostResponse
import io.example.board.domain.dto.response.common.Error
import io.example.board.domain.vo.login.LoginUser
import io.example.board.repository.MemberRepo
import io.example.board.repository.PostRepo
import io.example.board.service.listener.PostEvent
import io.example.board.service.listener.PostEventPublisher
import mu.KotlinLogging
import org.springframework.stereotype.Service
import javax.swing.event.DocumentEvent

/**
 * @author : choi-ys
 * @date : 2021/09/13 1:25 오후
 */
private val logger = KotlinLogging.logger {}

@Service
class PostService(
    private val memberRepo: MemberRepo,
    private val postRepo: PostRepo,
    private val postEventPublisher: PostEventPublisher,
) {
    fun post(postRequest: PostRequest, loginUser: LoginUser): PostResponse {
        val member = memberRepo.findByEmail(loginUser.email).orElseThrow() {
            val errorMessage = "요청에 해당하는 사용자가 없습니다."
            throw CommonException(Error(loginUser, errorMessage))
        }
        val savedPost = postRepo.save(postRequest.toEntity(member))
        val postResponse = PostResponse.mapFor(savedPost, member)
        postEventPublisher.notifyPosting(PostEvent(postResponse = postResponse, type = DocumentEvent.EventType.INSERT))
        return postResponse
    }

    fun loadPostById(postId: Long, loginUser: LoginUser): PostResponse {
        val member = memberRepo.findByEmail(loginUser.email).orElseThrow()
        val post = postRepo.findByIdAndMember(postId, member).orElseThrow()
        return PostResponse.mapFor(post, member)
    }
}