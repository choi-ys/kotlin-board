package io.example.board.domain.dto.request

import io.example.board.domain.entity.rdb.member.Member
import io.example.board.domain.entity.rdb.post.Post
import javax.validation.constraints.NotEmpty

/**
 * @author : choi-ys
 * @date : 2021/09/13 1:25 오후
 */
data class PostRequest(
    @field:NotEmpty(message = "제목을 입력하세요")
    val title: String,

    @field:NotEmpty(message = "본문을 입력하세요")
    val content: String,
) {
    fun toEntity(member: Member): Post {
        return Post(title = title, content = content, member = member)
    }
}
