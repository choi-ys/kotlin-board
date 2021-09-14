package io.example.board.service

import io.example.board.config.test.MockingTestConfig
import io.example.board.domain.dto.request.PostRequest
import io.example.board.domain.entity.rdb.member.Member
import io.example.board.domain.entity.rdb.member.MemberRole
import io.example.board.domain.entity.rdb.post.Post
import io.example.board.domain.vo.login.LoginUser
import io.example.board.repository.MemberRepository
import io.example.board.repository.PostRepository
import io.example.board.service.listener.PostEventPublisher
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.mockito.AdditionalAnswers
import org.mockito.BDDMockito.any
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import java.util.*

/**
 * @author : choi-ys
 * @date : 2021/09/14 4:48 오후
 */
@DisplayName("Service:Post")
internal class PostServiceTest : MockingTestConfig() {

    @Mock
    lateinit var memberRepository: MemberRepository

    @Mock
    lateinit var postRepository: PostRepository

    @Mock
    lateinit var postEventPublisher: PostEventPublisher

    @InjectMocks
    lateinit var postService: PostService

    @Test
    @DisplayName("게시글 생성")
    fun createPost() {
        // Given
        val postRequest = PostRequest("게시글 제목", "게시글 본문")
        val givenMember = Member(
            name = "choi-ys",
            email = "test@naver.com",
            password = "password",
            nickname = "noel",
            roles = mutableSetOf(MemberRole.MEMBER)
        )

        val loginUser = LoginUser(givenMember.email, givenMember.mapToSimpleGrantedAuthority())
        given(memberRepository.findByEmail(givenMember.email)).willReturn(Optional.of(givenMember))
        given(postRepository.save(any(Post::class.java))).will(AdditionalAnswers.returnsFirstArg<Any>())

        // When
        val postResponse = postService.post(postRequest = postRequest, loginUser = loginUser)

        // Then
        assertAll(
            { assertEquals(postResponse.title, postRequest.title) },
            { assertEquals(postResponse.content, postRequest.content) },
            { assertEquals(postResponse.writer.email, givenMember.email) },
            { assertEquals(postResponse.writer.name, givenMember.name) },
            { assertEquals(postResponse.writer.nickname, givenMember.nickname) },
        )
        verify(memberRepository, times(1)).findByEmail(givenMember.email)
        verify(postRepository, times(1)).save(any(Post::class.java))
    }
}