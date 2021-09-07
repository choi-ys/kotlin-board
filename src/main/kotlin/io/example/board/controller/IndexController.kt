package io.example.board.controller

import org.springframework.hateoas.IanaLinkRelations
import org.springframework.hateoas.Link
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author : choi-ys
 * @date : 2021/09/02 9:40 오전
 */
@RestController
@RequestMapping(
    value = ["index"],
    consumes = [MediaType.APPLICATION_JSON_VALUE],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
class IndexController {
    @GetMapping
    fun index(): Link = WebMvcLinkBuilder.linkTo(IndexController::class.java).withRel(IanaLinkRelations.INDEX)
}