package io.example.board.service

import io.example.board.aspect.exception.CommonException
import io.example.board.domain.dto.request.PostRequest
import io.example.board.domain.dto.response.PostResponse
import io.example.board.domain.dto.response.common.Error
import io.example.board.domain.vo.login.LoginUser
import io.example.board.repository.MemberRepository
import io.example.board.repository.PostRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service

/**
 * @author : choi-ys
 * @date : 2021/09/13 1:25 오후
 */
private val logger = KotlinLogging.logger {}

@Service
class PostService(
    private val memberRepository: MemberRepository,
    private val postRepository: PostRepository,
) {
    fun post(postRequest: PostRequest, loginUser: LoginUser): PostResponse {
        val member = memberRepository.findByEmail(loginUser.email).orElseThrow() {
            val errorMessage = "요청에 해당하는 사용자가 없습니다."
            throw CommonException(Error(loginUser, errorMessage))
        }
        val savedPost = postRepository.save(postRequest.toEntity(member))
        return PostResponse.mapFor(savedPost, member)
    }

    fun loadPostById(postId: Long, loginUser: LoginUser): PostResponse {
        val member = memberRepository.findByEmail(loginUser.email).orElseThrow()
        val post = postRepository.findByIdAndMember(postId, member).orElseThrow()
        return PostResponse.mapFor(post, member)
    }
}