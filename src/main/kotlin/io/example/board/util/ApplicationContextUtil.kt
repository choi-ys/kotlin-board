package io.example.board.util

import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

/**
 * @author : choi-ys
 * @date : 2021-08-30 오전 12:41
 */
@Component
class ApplicationContextUtil : ApplicationContextAware {
    @Throws(BeansException::class)
    override fun setApplicationContext(applicationContext: ApplicationContext) {
        Companion.applicationContext = applicationContext
    }

    companion object {
        private var applicationContext: ApplicationContext? = null
        fun <T> getBean(clazz: Class<T>): T {
            return applicationContext!!.getBean(clazz)
        }
    }
}