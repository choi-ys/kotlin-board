package io.example.board.repository

import io.example.board.domain.entity.rdb.post.Post
import org.springframework.data.jpa.repository.JpaRepository

/**
 * @author : choi-ys
 * @date : 2021/09/11 2:55 오전
 */
interface PostRepository : JpaRepository<Post, Long> {
}