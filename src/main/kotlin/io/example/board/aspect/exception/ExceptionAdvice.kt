package io.example.board.aspect.exception

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

private val logger = KotlinLogging.logger {  }

@RestControllerAdvice
class ExceptionAdvice<T> {

    @ExceptionHandler(CommonException::class)
    fun commonExceptionHandler(commonException: CommonException) : ResponseEntity<*>{
        val apiError = commonException.error
        logger.error("[{}]", apiError)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonException.error)
    }
}