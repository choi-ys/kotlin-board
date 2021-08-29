package io.example.board.config.p6spy

import com.p6spy.engine.spy.P6SpyOptions
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct

/**
 * @author : choi-ys
 * @date : 2021/04/13 5:52 오후
 * @Content : p6spy 의존성을 통한 실행 쿼리 출력 MessageFormat설정
 */
@Configuration
class P6spyLogMessageFormatConfiguration {
    @PostConstruct
    fun setLogMessageFormat() {
        P6SpyOptions.getActiveInstance().logMessageFormat = P6spySqlFormatConfiguration::class.java.name
    }
}