package com.example.degica_project.constants

enum class ApiErrorCodes(val errorCode: String, val errorMessage: String) {
    INVALID_REQUEST(getErrorCode("0001"), "INVALID_REQUEST"),
    DUPLICATE_REQUEST(getErrorCode("0002"), "DUPLICATE_REQUEST"),
    NOT_FOUND(getErrorCode("0003"), "NOT_FOUND"),
    INTERNAL_SERVER_ERROR(getErrorCode("0004"), "INTERNAL_SERVER_ERROR"),
    DATABASE_ERROR(getErrorCode("0005"), "DATABASE_ERROR");

    companion object {
        const val serviceErrorCode = "911"
    }
}
private fun getErrorCode(value: String): String {
    return ApiErrorCodes.serviceErrorCode + value
}
