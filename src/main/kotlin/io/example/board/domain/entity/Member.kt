package io.example.board.domain.entity

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
@SequenceGenerator(
    name = "MEMBER_ENTITY_SEQ_GENERATOR",
    sequenceName = "MEMBER_ENTITY_SEQ",
    initialValue = 1,
    allocationSize = 1
)
data class Member(
    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY,
        generator = "MEMBER_ENTITY_SEQ_GENERATOR"
    )
    @Column(name = "id", nullable = false, length = 50)
    val id: Long,

    @Column(name = "name", nullable = false, length = 15)
    var name: String,

    @Column(name = "email", nullable = false, length = 50)
    var email: String,

    @Column(name = "password", nullable = false, length = 75)
    var password: String,

    @Column(name = "nickname", nullable = false, length = 20)
    var nickname: String
){

    fun updateName(newName: String){
        this.name = newName
    }

    fun updateEmail(newEmail: String){
        this.email = newEmail
    }

    fun updatePassword(newPassword: String){
        this.password = newPassword
    }

    fun updateNickname(newNickname: String){
        this.nickname = newNickname
    }
}
