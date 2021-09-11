package io.example.board.domain.entity.rdb.post

import io.example.board.domain.entity.rdb.common.Auditor
import io.example.board.domain.entity.rdb.member.Member
import javax.persistence.*

/**
 * @author : choi-ys
 * @date : 2021/09/10 11:53 오후
 */
@Entity
@Table(name = "post_tb")
data class Post(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long = 0L,

    @Column(name = "title", nullable = false, length = 50)
    var title: String,

    @Lob
    var content: String,

    @Column(name = "view_count", nullable = false)
    var viewCount: Long = 0L,

    @Column(name = "display", nullable = false)
    var display: Boolean = true,

    @ManyToOne(fetch = FetchType.LAZY)
    var member: Member

) : Auditor() {
    fun updateTitle(newTitle: String) {
        title = newTitle
    }
}