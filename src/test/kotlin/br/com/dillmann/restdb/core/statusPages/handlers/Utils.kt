package br.com.dillmann.restdb.core.statusPages.handlers

import br.com.dillmann.restdb.testUtils.randomString
import io.ktor.application.ApplicationCall
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.http.RequestConnectionPoint
import io.ktor.request.ApplicationRequest
import io.ktor.response.ApplicationResponse
import io.ktor.response.ApplicationSendPipeline
import io.ktor.util.Attributes
import io.ktor.util.pipeline.PipelineContext
import io.mockk.*
import kotlinx.coroutines.runBlocking

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-03
 */
private typealias Handler<T> =
        suspend PipelineContext<Unit, ApplicationCall>.(T) -> Unit

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-03
 */
data class HandlerResponse(
    val statusCode: HttpStatusCode,
    val body: Any
)

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-03
 */
class HandlerFacade<T>(private val delegate: Handler<T>) : (T) -> HandlerResponse {

    private val context: PipelineContext<Unit, ApplicationCall> = mockk()
    private val statusCodeSlot = slot<HttpStatusCode>()
    private val bodySlot = slot<Any>()

    init {
        val call: ApplicationCall = mockk()
        val attributes: Attributes = mockk()
        val request: ApplicationRequest = mockk()
        val pipeline: ApplicationSendPipeline = mockk()
        val response: ApplicationResponse = mockk()
        val connectionPoint: RequestConnectionPoint = mockk()

        coEvery { pipeline.execute(any(), capture(bodySlot)) } answers { arg(1) }
        coEvery { attributes.getOrNull<Any>(any()) } returns null
        every { connectionPoint.uri } returns randomString()
        every { response.status(capture(statusCodeSlot)) } just Runs
        every { response.pipeline } returns pipeline
        every { call.request } returns request
        every { call.response } returns response
        every { call.attributes } returns attributes
        every { context.context } returns call
        every { request.call } returns call
        every { request.local } returns connectionPoint
    }

    override fun invoke(exception: T): HandlerResponse {
        runBlocking { delegate.invoke(context, exception) }
        return HandlerResponse(statusCodeSlot.captured, bodySlot.captured)
    }
}

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-03
 */
fun <T : Throwable> captureHandler(installer: StatusPages.Configuration.() -> Unit): HandlerFacade<T> {
    val slot = slot<Handler<T>>()
    val configuration: StatusPages.Configuration = mockk()
    every { configuration.exception(any(), capture(slot)) } just Runs
    configuration.installer()
    return HandlerFacade(slot.captured)
}