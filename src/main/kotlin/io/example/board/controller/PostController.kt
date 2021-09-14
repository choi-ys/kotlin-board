package io.example.board.controller

import io.example.board.domain.dto.request.PostRequest
import io.example.board.domain.vo.login.CurrentUser
import io.example.board.domain.vo.login.LoginUser
import io.example.board.service.PostService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

/**
 * @author : choi-ys
 * @date : 2021/09/13 2:32 오후
 */
@RestController
@RequestMapping(
    value = ["post"],
    consumes = [MediaType.APPLICATION_JSON_VALUE],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
class PostController(private val postService: PostService) {

    @PostMapping
    fun post(@Valid @RequestBody postRequest: PostRequest, @CurrentUser loginUser: LoginUser) =
        ResponseEntity.ok(postService.post(postRequest, loginUser))

    @GetMapping("{id}")
    fun postDetail(@PathVariable("id") postId: Long, @CurrentUser loginUser: LoginUser) =
        ResponseEntity.ok(postService.loadPostById(postId, loginUser))
}