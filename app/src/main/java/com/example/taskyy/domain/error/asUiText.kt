package com.example.taskyy.domain.error

import com.example.taskyy.R
import com.example.taskyy.ui.UiText

fun PasswordValidator.PasswordError.asUiText(): UiText {
    return when (this) {
        PasswordValidator.PasswordError.TOO_SHORT -> UiText.StringResource(
            R.string.too_short
        )

        PasswordValidator.PasswordError.NO_LOWERCASE -> UiText.StringResource(
            R.string.no_lowercase
        )

        PasswordValidator.PasswordError.NO_UPPERCASE -> UiText.StringResource(
            R.string.no_uppercase
        )

        PasswordValidator.PasswordError.NO_DIGIT -> UiText.StringResource(
            R.string.no_digit
        )
    }
}

fun Result.Error<*, PasswordValidator.PasswordError>.asErrorUiText(): UiText {
    return error.asUiText()
}