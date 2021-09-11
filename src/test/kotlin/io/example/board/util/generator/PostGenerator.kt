package io.example.board.util.generator

import io.example.board.domain.entity.rdb.post.Post
import io.example.board.repository.PostRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestComponent
import org.springframework.context.annotation.Import

/**
 * @author : choi-ys
 * @date : 2021/09/11 10:23 오후
 */
@TestComponent
@Import(MemberGenerator::class)
class PostGenerator {

    @Autowired
    lateinit var memberGenerator: MemberGenerator

    @Autowired
    lateinit var postRepository: PostRepository

    private val title = "게시판 제목"
    private val content = "게시판 본문"

    fun post(): Post {
        val savedMember = memberGenerator.savedMember()
        return Post(title = title, content = content, member = savedMember)
    }

    fun savedPost(): Post {
        return postRepository.saveAndFlush(post())
    }

}