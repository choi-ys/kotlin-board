package io.example.board.domain.entity.rdb.history

import io.example.board.domain.entity.rdb.common.Auditor
import io.example.board.domain.entity.rdb.common.EventType
import io.example.board.domain.entity.rdb.member.Member
import io.example.board.domain.entity.rdb.member.MemberStatus
import javax.persistence.*

/**
 * @author : choi-ys
 * @date : 2021-08-30 오전 12:01
 */
@Entity
@Table(name = "member_history_tb")
class MemberHistory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    var eventType: EventType,

    @ManyToOne
    @JoinColumn(name = "member_id")
    var member: Member,

    @Column(name = "name", nullable = false, length = 15)
    var name: String,

    @Column(name = "email", nullable = false, length = 50)
    var email: String,

    @Column(name = "password", nullable = false, length = 75)
    var password: String,

    @Column(name = "nickname", nullable = false, length = 20)
    var nickname: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    var status: MemberStatus?,

    @Column(name = "enabled", nullable = false, length = 20)
    var enabled: Boolean

) : Auditor() {

    companion object {
        fun mapFor(member: Member, eventType: EventType): MemberHistory {
            return MemberHistory(
                eventType = eventType,
                member = member,
                name = member.name,
                email = member.email,
                password = member.password,
                nickname = member.nickname,
                status = member.status,
                enabled = member.enabled
            )
        }
    }
}