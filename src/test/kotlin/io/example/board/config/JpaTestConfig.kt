package io.example.board.config

import io.example.board.config.base.BaseTestAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import javax.persistence.EntityManager

@DataJpaTest
class JpaTestConfig : BaseTestAnnotations() {

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