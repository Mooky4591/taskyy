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

fun DataError.Network.asUiText(): UiText {
    return when (this) {
        DataError.Network.UNKNOWN -> UiText.StringResource(
            R.string.unknown_network_error
        )

        DataError.Network.SERIALIZATION -> UiText.StringResource(
            R.string.serialization_error
        )

        DataError.Network.SERVER_ERROR -> UiText.StringResource(
            R.string.server_error
        )

        DataError.Network.PAYLOAD_TOO_LARGE -> UiText.StringResource(
            R.string.payload_too_large
        )

        DataError.Network.TOO_MANY_REQUESTS -> UiText.StringResource(
            R.string.too_many_requests
        )

        DataError.Network.REQUEST_TIMEOUT -> UiText.StringResource(
            R.string.request_timeout
        )

        DataError.Network.NO_INTERNET -> UiText.StringResource(
            R.string.no_internet
        )

        DataError.Network.UNKNOWN_HOST_EXCEPTION -> UiText.StringResource(
            R.string.unknown_host_exception
        )

        DataError.Network.INCORRECT_PASSWORD_OR_EMAIL -> UiText.StringResource(
            R.string.incorrect_password_or_email
        )
    }
}

fun Result.Error<*, PasswordValidator.PasswordError>.asErrorUiText(): UiText {
    return error.asUiText()
}