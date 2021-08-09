package io.example.board.controller

import io.example.board.aspect.log.Timer
import io.example.board.domain.dto.request.SignupRequest
import io.example.board.service.MemberService
import mu.KotlinLogging
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

private val logger = KotlinLogging.logger {  }

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
    fun signup(@RequestBody signupRequest: SignupRequest) : ResponseEntity<*>{
        return ResponseEntity.ok(memberService.signup(signupRequest))
    }
}