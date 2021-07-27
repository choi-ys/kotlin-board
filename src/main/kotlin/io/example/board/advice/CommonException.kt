package io.example.board.advice

class CommonException(val error: Error<*>) : RuntimeException()