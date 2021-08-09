package io.example.board.aspect.log

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

private val logger = LoggerFactory.getLogger(LoggingAspect::class.java)

@Aspect
@Component
class LoggingAspect {
    @Pointcut("execution(* io.example.board.controller..*.*(..))")
    fun pointCut() {
    }

    @Before("pointCut()")
    fun before(joinPoint: JoinPoint) {
        val signature = joinPoint.signature
        val declaringTypeName = signature.declaringTypeName
        val methodName = signature.name
        logger.info("Execution -> {}.{}()", declaringTypeName, methodName)

        val args = joinPoint.args
        args.forEach {
            logger.info("Args type -> {}", it.javaClass.name)
            logger.info("Args value -> {}", it)
        }
    }

    @AfterReturning(value = "pointCut()", returning = "returnValue")
    fun afterReturn(joinPoint: JoinPoint?, returnValue: Any?) = logger.info("Return -> {}",returnValue)
}