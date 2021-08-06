package io.example.board.config.test

import io.example.board.config.application.DataJpaAuditorConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import javax.persistence.EntityManager

@DataJpaTest
@Import(DataJpaAuditorConfig::class)
class JpaTestConfig : BaseTestConfig() {

    @Autowired
    lateinit var entityManager: EntityManager

    fun flush(){
        entityManager.flush()
    }

    fun clear(){
        entityManager.clear()
    }

    fun flushAndClear(){
        flush()
        clear()
    }
}