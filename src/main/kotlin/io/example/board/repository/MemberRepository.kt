package io.example.board.repository

import io.example.board.domain.entity.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long>{
    fun existsByEmail(email: String) : Boolean
}