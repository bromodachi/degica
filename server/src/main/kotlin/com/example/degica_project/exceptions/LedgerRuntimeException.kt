package com.example.degica_project.exceptions

import com.example.degica_project.constants.ApiErrorCodes
import org.springframework.http.HttpStatus

sealed class LedgerRuntimeException(
    open val status: HttpStatus,
    open val apiErrorCodes: ApiErrorCodes,
    override val message: String = "AN UNKNOWN ERROR OCCURRED"
) : RuntimeException()

class BadRequestException(
    override val status: HttpStatus = HttpStatus.BAD_REQUEST,
    override val apiErrorCodes: ApiErrorCodes = ApiErrorCodes.INVALID_REQUEST,
    override val message: String = "Bad request"
) : LedgerRuntimeException(status, apiErrorCodes, message)
class InternalServerException(
    override val status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
    override val apiErrorCodes: ApiErrorCodes = ApiErrorCodes.INTERNAL_SERVER_ERROR,
    override val message: String = "Internal server error"
) : LedgerRuntimeException(status, apiErrorCodes, message)

class DuplicateException(
    override val status: HttpStatus = HttpStatus.BAD_REQUEST,
    override val apiErrorCodes: ApiErrorCodes = ApiErrorCodes.DUPLICATE_REQUEST,
    override val message: String = "DUPLICATE REQUEST RECEIVED"
) : LedgerRuntimeException(status, apiErrorCodes, message)

class NotFoundException(
    override val status: HttpStatus = HttpStatus.BAD_REQUEST,
    override val apiErrorCodes: ApiErrorCodes = ApiErrorCodes.NOT_FOUND,
    override val message: String = "NOT FOUND"
) : LedgerRuntimeException(status, apiErrorCodes, message)

class DatabaseErrorException(
    override val status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
    override val apiErrorCodes: ApiErrorCodes = ApiErrorCodes.DATABASE_ERROR,
    override val message: String = "Database error"
) : LedgerRuntimeException(
    status,
    apiErrorCodes,
    message
)
