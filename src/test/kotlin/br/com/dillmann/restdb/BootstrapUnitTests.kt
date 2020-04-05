package br.com.dillmann.restdb

import br.com.dillmann.restdb.core.EmbeddedServer
import br.com.dillmann.restdb.core.jdbc.ConnectionDetails
import br.com.dillmann.restdb.core.jdbc.ConnectionValidator
import br.com.dillmann.restdb.core.log.LogLevelConfiguration
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-04
 */
class BootstrapUnitTests {

    @Before
    fun setUp() {
        mockkObject(EmbeddedServer, LogLevelConfiguration, ConnectionValidator, ConnectionDetails)
        every { EmbeddedServer.start() } just Runs
        every { LogLevelConfiguration.configure() } just Runs
        every { ConnectionValidator.checkJdbcConnectionState() } just Runs
        every { ConnectionDetails.printProductDetails() } just Runs
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `it should start embedded server`() {
        // execution
        main()

        // validation
        verify { EmbeddedServer.start() }
    }

    @Test
    fun `it should configure log level`() {
        // execution
        main()

        // validation
        verify { LogLevelConfiguration.configure() }
    }

    @Test
    fun `it should test connection to database using JDBC`() {
        // execution
        main()

        // validation
        verify { ConnectionValidator.checkJdbcConnectionState() }
    }

    @Test
    fun `it should print database and driver details`() {
        // execution
        main()

        // validation
        verify { ConnectionDetails.printProductDetails() }
    }
}