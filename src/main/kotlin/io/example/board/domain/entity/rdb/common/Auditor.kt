package io.example.board.domain.entity.rdb.common

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@EntityListeners(AuditingEntityListener::class)
@MappedSuperclass
open class Auditor(

    @CreationTimestamp  @Column(name = "created_date", updatable = false)
    open var createdDate: LocalDateTime? = null,

    @UpdateTimestamp @Column(name = "last_modified_date")
    open var lastModifiedDate: LocalDateTime? = null
)