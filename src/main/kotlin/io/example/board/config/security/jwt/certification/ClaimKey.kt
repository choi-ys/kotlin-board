package io.example.board.config.security.jwt.certification

/**
 * @author : choi-ys
 * @date : 2021/09/07 6:10 오후
 */
enum class ClaimKey(val value: String) {
    ISS("iss"),
    SUB("sub"),
    AUD("aud"),
    IAT("iat"),
    EXP("exp"),
    USE("use"),
    USERNAME("username"),
    AUTHORITIES("authorities");
}