package io.example.board.domain.vo.login

import org.springframework.security.core.annotation.AuthenticationPrincipal
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@AuthenticationPrincipal(expression = "#this =='anonymousUser' ? null : loginUser")
annotation class CurrentUser