package io.example.board.domain.vo.member

import io.example.board.domain.entity.rdb.member.Member

data class MemberSimpleInfo(
    val id: Long,
    val email: String,
    val name: String,
    val nickname: String,
) {
    companion object {
        fun mapFor(member: Member): MemberSimpleInfo {
            return MemberSimpleInfo(
                id = member.id,
                email = member.email,
                name = member.name,
                nickname = member.nickname
            )
        }
    }
}