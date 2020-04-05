package br.com.dillmann.restdb.core

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.*
import io.ktor.gson.gson
import io.ktor.http.ContentType

/**
 * Extension function for [Application] for Ktor content negotiation
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-26
 */
fun Application.installContentNegotiation() {
    install(ContentNegotiation) {
        gson(contentType = ContentType.Application.Json) { // JSON
            serializeNulls()
        }

        gson(contentType = ContentType.parse("application/merge-patch+json")) { // JSON Merge Patch
            serializeNulls()
        }
    }

    install(Compression) {
        gzip()
        deflate()
    }

    install(AutoHeadResponse)
}