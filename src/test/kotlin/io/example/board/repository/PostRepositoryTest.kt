package io.example.board.repository

import io.example.board.config.test.JpaTestConfig
import io.example.board.config.test.assertions.ObjectAssertions.Companion.assertPost
import io.example.board.domain.entity.rdb.post.Post
import io.example.board.util.generator.MemberGenerator
import io.example.board.util.generator.PostGenerator
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import

/**
 * @author : choi-ys
 * @date : 2021/09/11 2:58 오전
 */
@DisplayName("Repository:Post")
@Import(PostGenerator::class, MemberGenerator::class)
internal class PostRepositoryTest : JpaTestConfig() {

    @Autowired
    lateinit var memberGenerator: MemberGenerator

    @Autowired
    lateinit var postGenerator: PostGenerator

    @Autowired
    lateinit var postRepository: PostRepository

    @Test
    @DisplayName("게시글 객체 저장")
    fun save() {
        // Given
        val member = postGenerator.memberGenerator.savedMember()
        val title = "게시글 제목"
        val content = "게시글 본문"
        val post = Post(title = title, content = content, member = member)

        // When
        val expected = postRepository.saveAndFlush(post)

        // Then
        assertPost(entity = expected, given = post)
    }

    @Test
    @DisplayName("게시글 객체 조회")
    fun findById() {
        // Given
        val savedPost = postGenerator.savedPost()

        // When
        val expected = postRepository.findById(savedPost.id).orElseThrow()

        // Then
        assertPost(entity = expected, given = savedPost)
    }

    @Test
    @DisplayName("게시글 속성 수정")
    fun updateByDirtyChecking() {
        // Given
        val savedPost = postGenerator.savedPost()

        // When
        val newTitle = "수정된 게시글 제목"
        savedPost.updateTitle(newTitle)
        flushAndClear()

        // Then
        val expected = postRepository.findById(savedPost.id).orElseThrow()

        assertAll(
            { assertPost(entity = expected, given = savedPost) },
            { assertEquals(expected.title, newTitle) }
        )
    }

    @Test
    @DisplayName("게시글 객체 삭제")
    fun delete() {
        // Given
        val savedPost = postGenerator.savedPost()

        // When
        postRepository.delete(savedPost)
        flushAndClear()

        // Then
        assertThrows(NoSuchElementException::class.java) {
            postRepository.findById(savedPost.id).orElseThrow()
        }
    }

    @Test
    @DisplayName("특정 회원의 특정 게시글 조회")
    fun findByIdAndMember() {
        // Given
        val savedPost = postGenerator.savedPost()
        val postId = savedPost.id
        val member = savedPost.member
        flushAndClear()

        // When
        val expected = postRepository.findByIdAndMember(postId, member).orElseThrow()

        // Then
        assertAll(
            { assertNotNull(expected.id) },
            { assertEquals(expected.viewCount, 0L) },
            { assertEquals(expected.title, savedPost.title) },
            { assertEquals(expected.content, savedPost.content) },
            { assertEquals(expected.member, savedPost.member) },
            { assertNotNull(expected.createdAt) },
            { assertNotNull(expected.createdBy) },
            { assertNotNull(expected.updatedAt) },
            { assertNotNull(expected.updatedBy) }
        )
    }
}
