package io.example.board.domain.entity.rdb.member

import io.example.board.domain.entity.rdb.common.Auditor
import io.example.board.domain.entity.rdb.listener.MemberHistoryListener
import javax.persistence.*

@Entity
@Table(
    name = "member_tb",
    uniqueConstraints = [
        UniqueConstraint(
            name = "MEMBER_EMAIL_UNIQUE",
            columnNames = ["email"]
        )
    ]
)
@EntityListeners(MemberHistoryListener::class)
data class Member(

    @Column(name = "name", nullable = false, length = 15)
    var name: String,

    @Column(name = "email", nullable = false, length = 50)
    var email: String,

    @Column(name = "password", nullable = false, length = 75)
    var password: String,

    @Column(name = "nickname", nullable = false, length = 20)
    var nickname: String

) : Auditor() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, length = 50)
    val id: Long = 0L

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    var status: MemberStatus? = MemberStatus.UNCERTIFIED

    fun updateName(newName: String) {
        this.name = newName
    }

    fun updateEmail(newEmail: String) {
        this.email = newEmail
    }

    fun updatePassword(newPassword: String) {
        this.password = newPassword
    }

    fun updateNickname(newNickname: String) {
        this.nickname = newNickname
    }
}
