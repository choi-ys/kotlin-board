package io.example.board.repository

import io.example.board.config.test.JpaTestConfig
import io.example.board.config.test.assertions.ObjectAssertions.Companion.assertPost
import io.example.board.domain.entity.rdb.post.Post
import io.example.board.util.generator.PostGenerator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
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
@Import(PostGenerator::class)
internal class PostRepositoryTest : JpaTestConfig() {

    @Autowired
    lateinit var postGenerator: PostGenerator

    @Autowired
    lateinit var postRepository: PostRepository

    @Test
    @DisplayName("게시판 객체 저장")
    fun save() {
        // Given
        val member = postGenerator.memberGenerator.savedMember()
        val title = "게시판 제목"
        val content = "게시판 본문"
        val post = Post(title = title, content = content, member = member)

        // When
        val expected = postRepository.saveAndFlush(post)

        // Then
        assertPost(entity = expected, given = post)
    }

    @Test
    @DisplayName("게시판 객체 조회")
    fun findById() {
        // Given
        val savedPost = postGenerator.savedPost()

        // When
        val expected = postRepository.findById(savedPost.id).orElseThrow()

        // Then
        assertPost(entity = expected, given = savedPost)
    }

    @Test
    @DisplayName("게시판 속성 수정")
    fun updateByDirtyChecking() {
        // Given
        val savedPost = postGenerator.savedPost()

        // When
        val newTitle = "수정된 게시판 제목"
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
    @DisplayName("게시판 객체 삭제")
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
}
