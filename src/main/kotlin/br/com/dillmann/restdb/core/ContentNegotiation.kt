package br.com.dillmann.restdb.core

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.*
import io.ktor.http.ContentType
import io.ktor.jackson.jackson

/**
 * Extension function for [Application] for Ktor content negotiation
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-26
 */
fun Application.installContentNegotiation() {
    install(ContentNegotiation) {
        jackson(contentType = ContentType.Application.Json) { // JSON
            configureDefaults()
        }

        jackson(contentType = ContentType.parse("application/merge-patch+json")) { // JSON Merge Patch
            configureDefaults()
        }
    }

    install(Compression) {
        gzip()
        deflate()
    }

    install(AutoHeadResponse)
}

fun ObjectMapper.configureDefaults(): ObjectMapper {
    disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    enable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)
    enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    registerModule(KotlinModule())
    registerModule(Jdk8Module())
    registerModule(JavaTimeModule())
    setSerializationInclusion(JsonInclude.Include.ALWAYS)
    return this
}