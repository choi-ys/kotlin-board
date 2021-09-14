package io.example.board.aspect.exception

import io.example.board.domain.dto.response.common.DetailFieldError
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.validation.ConstraintViolationException

private val logger = KotlinLogging.logger { }

@RestControllerAdvice
class ExceptionAdvice<T> {

    // [500] Unchecked Exception
//    @ExceptionHandler(RuntimeException::class)
//    fun exceptionHandler(exception: RuntimeException): ResponseEntity<*> {
//        logger.info("[{}][{}][{}]", exception.javaClass.name, exception.localizedMessage, exception.stackTrace)
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예기치 못한 오류가 발생하였습니다.")
//    }

    // [500] Checked Exception
    @ExceptionHandler(CommonException::class)
    fun commonExceptionHandler(commonException: CommonException): ResponseEntity<*> {
        val apiError = commonException.error
        logger.error("[{}]", apiError)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(commonException.error)
    }

    // [400] @Valid를 이용한 유효성 검사 시, @RequestBody의 값이 없는 경우 JSR 380 Annotations이 적용된 필드의 Binding Exception
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun httpMessageNotReadableException(exception: HttpMessageNotReadableException): ResponseEntity<*> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("요청값이 올바르지 않습니다.")
    }

    // [400] @Valid를 이용한 유효성 검사 시, @RequestBody의 값이 잘못된 경우 JSR 380 Annotations이 적용된 필드의 Binding Exception
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun methodArgumentNotValidException(exception: MethodArgumentNotValidException): ResponseEntity<*> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(DetailFieldError.mapTo(exception.fieldErrors))
    }

    // [400] @Validated를 이용한 유효성 검사 시, @RequestParam의 값이 없는 경우 JSR 380 Annotations이 적용된 파라미터의 Binding Exception
    @ExceptionHandler(ConstraintViolationException::class)
    fun constraintViolationException(exception: Exception): ResponseEntity<*> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
    }

    // [400] @Validated를 이용한 유효성 검사 시, @RequestParam의 값이 잘못된 경우 JSR 380 Annotations이 적용된 파라미터의 Binding Exception
    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun missingServletRequestParameterException(exception: Exception): ResponseEntity<*> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
    }

    // [405] 허용하지 않는 Http Method 요청인 경우
    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun httpRequestMethodNotSupportedException(exception: Exception): ResponseEntity<*> {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("허용하지 않는 Http Method 요청입니다.")
    }

    // [415] 요청 Media Type이 잘못된 경우
    @ExceptionHandler(HttpMediaTypeNotSupportedException::class)
    fun httpMediaTypeNotSupportedException(exception: Exception): ResponseEntity<*> {
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("지원하지 않는 Mime 형식입니다.")
    }
}