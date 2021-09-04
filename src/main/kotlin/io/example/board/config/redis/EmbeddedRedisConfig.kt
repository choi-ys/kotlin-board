package io.example.board.config.redis

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.util.StringUtils
import redis.embedded.RedisServer
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy


private val logger = KotlinLogging.logger { }

/**
 * https://sabarada.tistory.com/106
 * https://jojoldu.tistory.com/297
 * https://derveljunit.tistory.com/310
 * https://blog.naver.com/gngh0101/221771979549
 * https://blog.naver.com/gngh0101/221780936975
 */
@Profile("local", "test") // profile이 local일때만 활성화
@Configuration
class EmbeddedRedisConfig {

    @Value("\${spring.redis.port}")
    private val redisPort = 0

    private var redisServer: RedisServer? = null
    private var os = OS.WINDOWS

    @PostConstruct
    @Throws(IOException::class)
    fun redisServer() {
        val port = if (isRedisRunning()) findAvailablePort() else redisPort
        logger.info("Embedded Redis start on [{}] by [{}] port", os, port)
        redisServer = RedisServer(port)
        redisServer!!.start()
    }

    private fun isRedisRunning() = isRunning(executeGrepProcessCommand(redisPort))

    private fun findAvailablePort(): Int {
        logger.info("find another port start")
        for (port in 10000..65535) {
            val process = executeGrepProcessCommand(port)
            if (!isRunning(process!!)) {
                logger.warn("Embedded Redis is running by [{}] port... find not using [{}] port", redisPort, port)
                return port
            }
        }

        throw IllegalArgumentException("Not Found Available port: 10000 ~ 65535")
    }

    @Throws(IOException::class)
    private fun executeGrepProcessCommand(port: Int): Process {
        os = if (System.getProperty("os.name").indexOf("Windows") > -1) OS.WINDOWS else OS.OTHERS
        var command: String
        var shell: Array<String>

        when (os) {
            OS.WINDOWS -> {
                command = String.format("netstat -nao | find LISTEN | find %d", port);
                shell = arrayOf("cmd.exe", "/y", "/c", command)
            }
            else -> {
                command = String.format("netstat -nat | grep LISTEN|grep %d", port)
                shell = arrayOf("/bin/sh", "-c", command)
            }
        }
        return Runtime.getRuntime().exec(shell)
    }

    private fun isRunning(process: Process): Boolean {
        var line: String = ""
        val pidInfo = StringBuilder()
        try {
            BufferedReader(InputStreamReader(process.inputStream)).use { input ->
                while (input.readLine().also {
                        line = it
                    } != null) {
                    pidInfo.append(line)
                }
            }
        } catch (e: Exception) {
        }

        logger.info(
            "pid info [{}] by execute command [{}], check isRunning -> {}",
            pidInfo, line, StringUtils.isEmpty(pidInfo.toString())
        )
        return !StringUtils.isEmpty(pidInfo.toString())
    }

    @PreDestroy
    fun stopRedis() {
        redisServer?.stop()
    }
}

enum class OS {
    WINDOWS,
    OTHERS,
}