package com.example.taskyy.domain.error

class PasswordValidator {

    fun validatePassword(password: String): Result<Unit, PasswordError> {
        if (password.length < 8) {
            return Result.Error(PasswordError.TOO_SHORT)
        }
        if (!password.any { it.isLowerCase() }) {
            return Result.Error(PasswordError.NO_LOWERCASE)
        }
        if (!password.any { it.isUpperCase() }) {
            return Result.Error(PasswordError.NO_UPPERCASE)
        }
        if (!password.any { it.isDigit() }) {
            return Result.Error(PasswordError.NO_DIGIT)
        }
        return Result.Success(Unit)
    }

    enum class PasswordError : Error {
        TOO_SHORT,
        NO_UPPERCASE,
        NO_LOWERCASE,
        NO_DIGIT
    }
}