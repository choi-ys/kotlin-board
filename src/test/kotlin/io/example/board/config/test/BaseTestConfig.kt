package io.example.board.config.test

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName::class)
@Disabled
open class BaseTestConfig