package com.example.taskyy.domain.error

interface LocalDataErrorHelper {
    companion object {
        fun determineLocalDataErrorMessage(message: String): Result<Any, DataError.Local> {
            return when (message) {
                "Permission denied" -> Result.Error(DataError.Local.PERMISSION_DENIED)
                "File not found" -> Result.Error(DataError.Local.FILE_NOT_FOUND)
                "Disk full" -> Result.Error(DataError.Local.DISK_FULL)
                "Input/output error" -> Result.Error(DataError.Local.INPUT_OUTPUT_ERROR)
                "Connection refused" -> Result.Error(DataError.Local.CONNECTION_REFUSED)
                else -> Result.Error(DataError.Local.UNKNOWN)
            }
        }
    }
}