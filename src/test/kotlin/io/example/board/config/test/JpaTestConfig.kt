package io.example.board.config.test

import com.github.gavlyukovskiy.boot.jdbc.decorator.DataSourceDecoratorAutoConfiguration
import io.example.board.config.application.DataJpaAuditorConfig
import io.example.board.config.p6spy.P6spyLogMessageFormatConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import javax.persistence.EntityManager

@DataJpaTest(showSql = false)
@ImportAutoConfiguration(DataSourceDecoratorAutoConfiguration::class)
@Import(DataJpaAuditorConfig::class, P6spyLogMessageFormatConfiguration::class)
class JpaTestConfig : BaseTestConfig(){

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