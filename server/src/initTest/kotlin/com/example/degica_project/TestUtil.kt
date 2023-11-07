package com.example.degica_project

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.core.io.DefaultResourceLoader
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader
import org.springframework.util.FileCopyUtils
import java.io.IOException
import java.io.InputStreamReader
import java.io.UncheckedIOException
import java.nio.charset.StandardCharsets

class TestUtil private constructor() {
    val objectMapper: ObjectMapper = jacksonObjectMapper()

    private fun asString(resource: Resource): String? {
        try {
            InputStreamReader(resource.inputStream, StandardCharsets.UTF_8).use { reader ->
                return FileCopyUtils.copyToString(
                    reader
                )
            }
        } catch (e: IOException) {
            throw UncheckedIOException(e)
        }
    }

    fun readAsString(path: String?): String? {
        val resourceLoader: ResourceLoader = DefaultResourceLoader()
        return asString(resourceLoader.getResource(path!!))
    }

    @Throws(JsonProcessingException::class)
    fun <T> readJson(path: String?, clazz: Class<T>?): T {
        return objectMapper.readValue(readAsString(path), clazz)
    }

    @Throws(JsonProcessingException::class)
    fun <T> readREsponse(path: String?, clazz: Class<T>?): T {
        return objectMapper.readValue(readAsString(path), clazz)
    }

    @Throws(JsonProcessingException::class)
    fun toJsonNode(s: String?): JsonNode? {
        return objectMapper.readTree(s)
    }

    @Throws(JsonProcessingException::class)
    fun writeValueAsString(o: Any?): String? {
        return objectMapper.writeValueAsString(o)
    }

    companion object {
        val instance: TestUtil by lazy {
            TestUtil()
        }
    }
}
