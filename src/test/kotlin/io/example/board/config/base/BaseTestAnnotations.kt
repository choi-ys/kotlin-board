package io.example.board.config.base

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("TEST")
@TestMethodOrder(MethodOrderer.MethodName::class)
@Disabled
open class BaseTestAnnotations