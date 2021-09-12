package io.example.board.aspect.exception

import io.example.board.domain.dto.response.common.Error

class CommonException(val error: Error<*>) : RuntimeException()