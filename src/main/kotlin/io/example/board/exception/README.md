예외 처리
===
- 문제 : 로직별로 예외 처리를 하게 되며 추가 되는 Custom Exception handler method가 지속적으로 증가(관리 포인트의 증가) 
- 구현 : @RestControllerAdvice와 @ExceptionHandler를 이용한 Exception Handling

---

CommonException
```kotlin
open class CommonException(
    val code: String,
    message: String,
    val status: HttpStatus,
) : RuntimeException(message)
```

ExceptionHandler
```kotlin
@RestControllerAdvice
class ExceptionHandler {

    // Custom Exception Handling via CommonException Handler
    @ExceptionHandler(CommonException::class)
    fun commonExceptionHandler(e: CommonException) =
        ResponseEntity(
            CommonExceptionResponse(
                code = e.code,
                message = e.message?: "알 수 없는 오류",
            ),
            e.status,
        )
    
    // 500 Error Exception Handling
    @ExceptionHandler(RuntimeException::class)
    fun runtimeExceptionHandler(e: RuntimeException): ResponseEntity<CommonExceptionResponse> {
        e.printStackTrace()
        return ResponseEntity(
            CommonExceptionResponse(
                code = "INTERNAL_SERVER_ERROR",
                message = e.message?: "알 수 없는 오류",
            ),
            HttpStatus.INTERNAL_SERVER_ERROR,
        )
    }
}
```

참고 URL : [Exception Handling](https://velog.io/@dhwlddjgmanf/%EA%BC%AC%EB%A6%AC%EB%B3%84-%EC%98%A4%EB%A5%98%EC%9D%BC%EC%A7%80-Exception-Handling%EC%9D%84-%EC%96%B4%EB%96%BB%EA%B2%8C-%ED%95%98%EB%A9%B4-%EB%8D%94-%EC%9E%98%ED%95%A0-%EC%88%98-%EC%9E%88%EC%9D%84%EA%B9%8C)
