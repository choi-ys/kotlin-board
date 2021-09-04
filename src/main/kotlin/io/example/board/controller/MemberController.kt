package io.example.board.controller

import io.example.board.aspect.log.Timer
import io.example.board.domain.dto.request.SignupRequest
import io.example.board.service.MemberService
import mu.KotlinLogging
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

private val logger = KotlinLogging.logger { }

@RestController
@RequestMapping(
    value = ["member"],
    consumes = [MediaType.APPLICATION_JSON_VALUE],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
class MemberController(
    private val memberService: MemberService
) {

    @Timer
    @PostMapping("signup")
    fun signup(@Valid @RequestBody signupRequest: SignupRequest): ResponseEntity<*> {
        return ResponseEntity.ok(memberService.signup(signupRequest))
    }
}