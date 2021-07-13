package io.example.board.repository

import io.example.board.domain.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
//import org.springframework.stereotype.Repository

interface MemberRepository : JpaRepository<Member, Long>