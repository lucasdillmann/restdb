package br.com.dillmann.restdb

import br.com.dillmann.restdb.core.installContentNegotiation
import br.com.dillmann.restdb.core.installCors
import br.com.dillmann.restdb.core.installRequestTracing
import br.com.dillmann.restdb.core.statusPages.fallbackResponse
import br.com.dillmann.restdb.core.statusPages.installErrorHandlers
import br.com.dillmann.restdb.domain.data.dataEndpoints
import br.com.dillmann.restdb.domain.index.indexEndpoints
import br.com.dillmann.restdb.domain.metadata.metadataEndpoints
import io.ktor.application.Application
import io.ktor.routing.Routing
import io.ktor.routing.routing
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-05-09
 */
class ApplicationUnitTests {

    private val application = mockk<Application>()
    private val routing = mockk<Routing>()

    @Before
    fun setUp() {
        mockkStatic(
            "br.com.dillmann.restdb.core.ContentNegotiationKt",
            "br.com.dillmann.restdb.core.RequestTracingKt",
            "br.com.dillmann.restdb.core.statusPages.HttpErrorHandlerKt",
            "br.com.dillmann.restdb.core.CORSKt",
            "br.com.dillmann.restdb.domain.metadata.DatabaseMetadataRestControllerKt",
            "br.com.dillmann.restdb.domain.data.DataRestControllerKt",
            "br.com.dillmann.restdb.domain.index.IndexRestControllerKt",
            "br.com.dillmann.restdb.core.statusPages.HttpFallbackResponseKt",
            "io.ktor.routing.RoutingKt"
        )

        every { application.installContentNegotiation() } just Runs
        every { application.installRequestTracing() } just Runs
        every { application.installErrorHandlers() } just Runs
        every { application.installCors() } just Runs
        every { routing.metadataEndpoints() } just Runs
        every { routing.dataEndpoints() } just Runs
        every { routing.indexEndpoints() } just Runs
        every { routing.fallbackResponse() } just Runs

        every { application.routing(any()) } answers {
            val action: Routing.() -> Unit = arg(1)
            routing.action()
            routing
        }
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `application module should install content negotiation`() {
        // execution
        application.module()

        // validation
        verify { application.installContentNegotiation() }
    }

    @Test
    fun `application module should install request tracing`() {
        // execution
        application.module()

        // validation
        verify { application.installRequestTracing() }
    }

    @Test
    fun `application module should install error handlers`() {
        // execution
        application.module()

        // validation
        verify { application.installErrorHandlers() }
    }

    @Test
    fun `application module should install CORS`() {
        // execution
        application.module()

        // validation
        verify { application.installCors() }
    }

    @Test
    fun `application module should install metadata endpoint routes`() {
        // execution
        application.module()

        // validation
        verify { routing.metadataEndpoints() }
    }

    @Test
    fun `application module should install data endpoint routes`() {
        // execution
        application.module()

        // validation
        verify { routing.dataEndpoints() }
    }

    @Test
    fun `application module should install index endpoint routes`() {
        // execution
        application.module()

        // validation
        verify { routing.indexEndpoints() }
    }

    @Test
    fun `application module should install fallback response routes`() {
        // execution
        application.module()

        // validation
        verify { routing.fallbackResponse() }
    }

}