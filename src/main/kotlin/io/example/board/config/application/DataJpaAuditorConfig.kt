package io.example.board.config.application

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.util.*

@Configuration
@EnableJpaAuditing
class DataJpaAuditorConfig {

    // TODO JWT의 Principal을 이용한 Auditing(CreatedBy, UpdatedBy) 정보 설정
    @Bean
    fun auditorProvider(): AuditorAware<String> {
        return AuditorAware { Optional.of(UUID.randomUUID().toString()) }
    }
}