package io.example.board.domain.dto.response

import io.example.board.domain.entity.rdb.member.Member
import io.example.board.domain.entity.rdb.post.Post
import io.example.board.domain.vo.member.MemberSimpleInfo

data class PostResponse(
    val id: Long,
    val title: String,
    val content: String,
    val viewCount: Long,
    val display: Boolean,
    val writer: MemberSimpleInfo,
) {
    companion object {
        fun mapFor(post: Post, member: Member): PostResponse {
            return PostResponse(
                id = post.id,
                title = post.title,
                content = post.content,
                viewCount = post.viewCount,
                display = post.display,
                writer = MemberSimpleInfo.mapFor(member)
            )
        }
    }
}
