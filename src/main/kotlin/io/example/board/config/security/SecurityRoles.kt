package io.example.board.config.security

import org.springframework.http.HttpMethod

enum class SecurityRoles(private val matchers: List<AuthRequest>) {
    NONE(listOf<AuthRequest>(
        AuthRequest(
            HttpMethod.GET, listOf(
            "/index"
        )),
        AuthRequest(
            HttpMethod.POST, listOf(
            "/signup", "/login"
        ))
    ));

    fun patterns(method: HttpMethod): Array<String> {
        return matchers.find { it.method == method }?.patterns?.toTypedArray() ?: arrayOf()
    }
}

data class AuthRequest(
    val method: HttpMethod,
    val patterns: List<String> = listOf(),
)