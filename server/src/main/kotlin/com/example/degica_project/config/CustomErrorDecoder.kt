package com.example.degica_project.config

import feign.Response
import feign.RetryableException
import feign.codec.ErrorDecoder
import java.lang.Exception

class CustomErrorDecoder : ErrorDecoder {
    override fun decode(methodKey: String, response: Response): Exception {
        val exception = feign.FeignException.errorStatus(methodKey, response)
        if (response.status() in 500..599) {
            return RetryableException(
                response.status(),
                exception.message,
                response.request().httpMethod(),
                exception,
                null,
                response.request()
            )
        }
        return exception
    }
}
