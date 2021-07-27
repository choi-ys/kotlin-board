package io.example.board.domain.vo.login

import org.springframework.security.core.GrantedAuthority

data class LoginUser(
    var email: String,
    var authorities: Collection<GrantedAuthority?>? = null
)
