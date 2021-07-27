package io.example.board.domain.vo.login

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

class LoginUserAdapter(username: String, authorities: Collection<GrantedAuthority>?) : User(username, "", authorities) {
    val loginUser: LoginUser = LoginUser(username, authorities)
}