package io.example.board.repository

import io.example.board.domain.entity.rdb.member.Member
import io.example.board.domain.entity.rdb.post.Post
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

/**
 * @author : choi-ys
 * @date : 2021/09/11 2:55 오전
 */
interface PostRepo : JpaRepository<Post, Long> {

    @EntityGraph(attributePaths = ["member"])
    fun findByIdAndMember(postId: Long, member: Member): Optional<Post>
}