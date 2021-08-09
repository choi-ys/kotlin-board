package io.example.board.aspect.exception

class CommonException(val error: Error<*>) : RuntimeException()