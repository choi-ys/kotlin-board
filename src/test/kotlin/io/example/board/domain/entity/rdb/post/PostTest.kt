package io.example.board.domain.entity.rdb.post

import io.example.board.util.generator.MemberGenerator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

/**
 * @author : choi-ys
 * @date : 2021/09/11 12:09 오전
 */
internal class PostTest {

    @Test
    @DisplayName("게시글 객체 생성")
    fun save() {
        // Given
        val member = MemberGenerator.member()
        val title = "게시글 제목"
        val content = "게시글 본문"
        val post = Post(title = title, content = content, member = member)

        // When
        assertAll(
            { assertEquals(post.id, 0L) },
            { assertEquals(post.title, title) },
            { assertEquals(post.content, content) },
            { assertEquals(post.viewCount, 0L) },
            { assertEquals(post.display, true, "생성자에서 제외된 전시 항목의 기본 전시 여부를 확인") },
            { assertEquals(post.createdBy, null, "Auditor를 통해 설정되는 생성주체 정보의 null 여부를 확인") },
            { assertEquals(post.updatedBy, null, "Auditor를 통해 설정되는 수정주체 정보의 null 여부를 확인") },
            { assertEquals(post.createdAt, null, "Auditor를 통해 설정되는 생성일자 정보의 null 여부를 확인") },
            { assertEquals(post.updatedAt, null, "Auditor를 통해 설정되는 수정일자 정보의 null 여부를 확인") }
        )
    }

    @Test
    @DisplayName("게시글 제목 변경")
    fun updateTitle() {
        // Given
        val title = "게시글 제목"
        val newTitle = "수정된 게시글 제목"
        val content = "게시글 본문"
        val member = MemberGenerator.member()
        val post = Post(title = title, content = content, member = member)

        // When
        post.updateTitle(newTitle)

        // Then
        assertAll(
            { assertEquals(post.id, 0L) },
            { assertEquals(post.title, newTitle, "게시글 제목의 변경 여부를 확인") },
            { assertEquals(post.content, content) },
            { assertEquals(post.viewCount, 0L) },
            { assertEquals(post.display, true, "생성자에서 제외된 전시 항목의 기본 전시 여부 항목을 확인") },
            { assertEquals(post.createdBy, null, "Auditor를 통해 설정되는 생성주체 정보의 null 여부를 확인") },
            { assertEquals(post.updatedBy, null, "Auditor를 통해 설정되는 수정주체 정보의 null 여부를 확인") },
            { assertEquals(post.createdAt, null, "Auditor를 통해 설정되는 생성일자 정보의 null 여부를 확인") },
            { assertEquals(post.updatedAt, null, "Auditor를 통해 설정되는 수정일자 정보의 null 여부를 확인") }
        )
    }
}