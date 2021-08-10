package io.example.board.aspect.exception

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.validation.ConstraintViolationException

private val logger = KotlinLogging.logger {  }

@RestControllerAdvice
class ExceptionAdvice<T> {

    // unexpected Exception
    @ExceptionHandler(Exception::class)
    fun exceptionHandler(exception: Exception) : ResponseEntity<*>{
        logger.info("[{}][{}][{}]", exception.javaClass.name, exception.localizedMessage, exception.stackTrace)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예기치 못한 오류가 발생하였습니다.")
    }

    // expected Exception
    @ExceptionHandler(CommonException::class)
    fun commonExceptionHandler(commonException: CommonException) : ResponseEntity<*>{
        val apiError = commonException.error
        logger.error("[{}]", apiError)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(commonException.error)
    }

    // @Valid를 이용하여 @RequestBody에 적용된 JSR 380 Annotations의 invalid exception -> 400 error
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun methodArgumentNotValidException(): ResponseEntity<*> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
    }

    // 요청 파라미터에 값이 잘못된 경우
    // @Validated를 이용하여 @RequestParam에 적용된 JSR 380 Annotations의 invalid exception -> 400 error
    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun missingServletRequestParameterException(): ResponseEntity<*> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
    }

    // 요청 파라미터에 값이 없는 경우
    // @Validated를 이용하여 @RequestParam에 적용된 JSR 380 Annotations의 invalid exception -> 400 error
    @ExceptionHandler(ConstraintViolationException::class)
    fun constraintViolationException(): ResponseEntity<*> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
    }
}