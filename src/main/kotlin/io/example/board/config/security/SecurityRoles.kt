package io.example.board.config.security

import org.springframework.http.HttpMethod

enum class SecurityRoles(private val matchers: List<AuthRequest>) {
    NONE(listOf<AuthRequest>(
        AuthRequest(
            HttpMethod.GET, listOf(
            "/post/**"
        )),
        AuthRequest(
            HttpMethod.POST, listOf(
            "/signup", "/login"
        ))
    )),
    USER(listOf<AuthRequest>(
        AuthRequest(
            HttpMethod.GET, listOf(
            "/post/**"
        )),
        AuthRequest(
            HttpMethod.POST, listOf(
            "/login", "/logout"
        )),
        AuthRequest(
            HttpMethod.PUT, listOf(
            "/member/**"
        )),
    )),
    ADMIN(listOf<AuthRequest>(
        AuthRequest(HttpMethod.GET, listOf(
            "/**"
        )),
        AuthRequest(HttpMethod.POST, listOf(
            "/**"
        )),
        AuthRequest(HttpMethod.PUT, listOf(
            "/**"
        )),
        AuthRequest(HttpMethod.DELETE, listOf(
            "/**"
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