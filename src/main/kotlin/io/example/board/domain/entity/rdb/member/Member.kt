package io.example.board.domain.entity.rdb.member

import io.example.board.domain.entity.rdb.common.Auditor
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.stream.Collectors
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
data class Member(

    @Column(name = "name", nullable = false, length = 15)
    var name: String,

    @Column(name = "email", nullable = false, length = 50)
    var email: String,

    @Column(name = "password", nullable = false, length = 75)
    var password: String,

    @Column(name = "nickname", nullable = false, length = 20)
    var nickname: String,

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "member_role_tb",
        joinColumns = [JoinColumn(
            name = "member_id",
            foreignKey = ForeignKey(name = "TB_MEMBER_ROLE_MEMBER_ID_FOREIGN_KEY")
        )]
    )
    @Enumerated(EnumType.STRING)
    var roles: MutableSet<MemberRole> = mutableSetOf(MemberRole.MEMBER),

    @Column(name = "enabled", nullable = false)
    var enabled: Boolean = true

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

    fun mapToSimpleGrantedAuthority(): Set<SimpleGrantedAuthority> {
        return roles.stream()
            .map { SimpleGrantedAuthority("ROLE_" + it.name) }
            .collect(Collectors.toSet())
    }

    fun addRoles(additionRoles: Set<MemberRole>) {
        roles.addAll(additionRoles)
    }

    fun removeRoles(removalRoles: Set<MemberRole>) {
        if (removalRoles == roles) {
            throw IllegalArgumentException("최소 하나 이상의 권한이 존재해야 합니다.")
        }
        roles.removeAll(removalRoles)
    }
}
