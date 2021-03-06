package br.com.dillmann.restdb.core.jdbc

import br.com.dillmann.restdb.domain.metadata.resolver.MetadataResolver
import br.com.dillmann.restdb.domain.metadata.resolver.MetadataResolverFactory
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.sql.Connection

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-03
 */
class ConnectionDetailsUnitTests {

    private val connection: Connection = mockk()
    private val metadataResolver: MetadataResolver = mockk()
    private val productDetails = randomProductDetails()

    @Before
    fun setUp() {
        mockkObject(ConnectionPool, MetadataResolverFactory, logger)

        every { connection.close() } just Runs
        every { logger.info(any()) } just Runs
        every { ConnectionPool.startConnection() } returns connection
        every { MetadataResolverFactory.build() } returns metadataResolver
        every { metadataResolver.findProductDetails(any()) } returns productDetails
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `it should get product details from jdbc connection and print them using log`() {
        // scenario
        val (database, driver) = productDetails
        val expectedMessage = "Connection started successfully to database $database using $driver"

        // execution
        ConnectionDetails.printProductDetails()

        // validation
        verify { logger.info(expectedMessage) }
    }

    @Test
    fun `it should close connection after its use`() {
        // execution
        ConnectionDetails.printProductDetails()

        // validation
        verify { connection.close() }
    }
}