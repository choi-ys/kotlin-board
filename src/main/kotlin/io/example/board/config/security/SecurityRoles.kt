package io.example.board.config.security

import org.springframework.http.HttpMethod

enum class SecurityRoles(private val matchers: List<AuthRequest>) {
    NONE(listOf(
            AuthRequest(
                HttpMethod.GET, listOf(
                    "/index"
                )),
            AuthRequest(
                HttpMethod.POST, listOf(
                    "/member/signup", "/login"
                ))
    )),
    MEMBER(
        listOf(
            AuthRequest(
                HttpMethod.GET, listOf(
                    "/member/roles"
                )),
            AuthRequest(
                HttpMethod.POST, listOf(
                    "/refresh"
                )),
    ));

    fun patterns(method: HttpMethod): Array<String> {
        return matchers.find { it.method == method }?.patterns?.toTypedArray() ?: arrayOf()
    }
}

data class AuthRequest(
    val method: HttpMethod,
    val patterns: List<String> = listOf(),
)