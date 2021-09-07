package io.example.board.domain.vo.login.token

import com.fasterxml.jackson.annotation.JsonFormat
import java.util.*

/**
 * @author : choi-ys
 * @date : 2021/09/01 7:20 오후
 */
data class Token(
    val accessToken: String,

    private val refreshToken: String,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss", timezone = "Asia/Seoul")
    val accessExpired: Date
)