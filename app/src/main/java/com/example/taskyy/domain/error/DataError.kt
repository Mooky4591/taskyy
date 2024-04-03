package com.example.taskyy.domain.error

sealed interface DataError : Error {
    enum class Network : DataError {
        REQUEST_TIMEOUT,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        PAYLOAD_TOO_LARGE,
        SERVER_ERROR,
        SERIALIZATION,
        INCORRECT_PASSWORD_OR_EMAIL,
        UNKNOWN,
        UNKNOWN_HOST_EXCEPTION
    }

    enum class Local : DataError {
        DISK_FULL,
        PERMISSION_DENIED,
        FILE_NOT_FOUND,
        INPUT_OUTPUT_ERROR,
        CONNECTION_REFUSED,
        UNKNOWN
    }

}