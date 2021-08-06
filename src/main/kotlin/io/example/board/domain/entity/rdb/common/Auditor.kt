package io.example.board.domain.entity.rdb.common

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@EntityListeners(AuditingEntityListener::class)
@MappedSuperclass
abstract class Auditor(

    @CreatedBy @Column(name = "created_by", updatable = false)
    var createdBy: String? = null,

    @LastModifiedBy @Column(name = "updated_by")
    var updatedBy: String? = null,

    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    var createdDate: LocalDateTime? = null,

    @UpdateTimestamp
    @Column(name = "updated_date")
    var updatedDate: LocalDateTime? = null,
)