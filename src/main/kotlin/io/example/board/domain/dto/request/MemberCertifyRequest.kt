package io.example.board.domain.dto.request

data class MemberCertifyRequest(
    var id: Long,
    var email: String,
    var certificationTest: String
)