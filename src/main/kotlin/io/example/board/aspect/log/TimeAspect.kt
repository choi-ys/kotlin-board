package io.example.board.aspect.log

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.util.StopWatch

private val logger = LoggerFactory.getLogger(TimeAspect::class.java)

@Aspect
@Component
class TimeAspect {
    @Pointcut("execution(* io.example.board.controller..*.*(..))")
    fun pointCut() {
    }

    @Pointcut("@annotation(io.example.board.aspect.log.Timer)")
    fun enableTimer() {
    }

    @Around("pointCut() && enableTimer()")
    fun around(proceedingJoinPoint: ProceedingJoinPoint) : Any{
        val stopwatch = StopWatch()
        stopwatch.start()

        val returnValue = proceedingJoinPoint.proceed()

        stopwatch.stop()
        logger.info("Total Time : {}", stopwatch.totalTimeSeconds)
        return returnValue
    }
}