package io.example.board.aspect.exception

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

/**
 * @author : choi-ys
 * @date : 2021-04-24 오후 9:06
 * @apiNote API 처리 중 발생하는 오류 응답 반환 객체
 *
 *  *  timeStamp : 오류 일시 (응답 미포함)
 *  *  message : 개발자가 정의한 오류 내용
 *  *  traceMessage : 디버깅을 위한 exception stack trace (응답 미포함)
 *  *  request : Client 측 요청 데이터
 *  *  errorDetails
 *  *  * 오류가 발생한 요청 데이터
 *  *  * validationErrorList : JSR 303 Annocations을 이용한 객체 바인딩 시점의 유효성 검사에서 발생한 오류
 */
data class Error<T> (
    @JsonIgnore
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
    var timeStamp: LocalDateTime = LocalDateTime.now()
) {
    var message: String? = null

    @JsonIgnore
    var traceMessage: String? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var request: T? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var errorDetails: T? = null

    constructor(exception: Throwable) : this() {
        message = exception.message
        traceMessage = exception.localizedMessage
    }

    constructor(request: T, message: String?) : this() {
        this.message = message
        this.request = request
    }

    constructor(exception: Throwable, request: T) : this(exception) {
        this.request = request
    }

    constructor(exception: Throwable, request: T, errorDetails: T) : this(exception, request) {
        this.errorDetails = errorDetails
    }
}