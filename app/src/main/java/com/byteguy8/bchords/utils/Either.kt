package com.byteguy8.bchords.utils

import java.lang.IllegalStateException

sealed class Either<out L, out R> {
    inline fun <reified S, T> flatMap(fn: (R) -> Either<S, T>): Either<S, T> {
        return when (this) {
            is Left -> Left(error as S)
            is Right -> fn(value)
        }
    }

    fun <T> fold(fn: (R) -> T): Either<L, T> {
        return when (this) {
            is Left -> this
            is Right -> Right(fn(value))
        }
    }

    fun rightOrNull(): R? {
        return when (this) {
            is Left -> null
            is Right -> value
        }
    }

    fun rightOrException(fn: (error: L) -> String?): R {
        return when (this) {
            is Left -> throw IllegalStateException(fn(error) ?: "Unknown error")
            is Right -> value
        }
    }
}

class Left<L>(val error: L) : Either<L, Nothing>()
class Right<R>(val value: R) : Either<Nothing, R>()