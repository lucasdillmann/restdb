package br.com.dillmann.restdb.core.jdbc.type

import br.com.dillmann.restdb.core.environment.EnvironmentVariables
import br.com.dillmann.restdb.testUtils.expectMatch
import br.com.dillmann.restdb.testUtils.randomString
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import java.lang.reflect.InvocationTargetException
import kotlin.test.assertEquals

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-05-09
 */
class DriverTypeResolverUnitTests {

    @get:Rule
    val expectedException: ExpectedException = ExpectedException.none()

    @Before
    fun setUp() {
        mockkObject(EnvironmentVariables)
        DriverTypeResolver.resetLazyValue()
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `when JDBC URL is for a PostgreSQL database, it should detect it correctly`() {
        // scenario
        val expectedResult = DriverType.POSTGRESQL
        val jdbcUrl = "jdbc:postgresql://${randomString()}/${randomString()}"
        every { EnvironmentVariables.jdbcUrl } returns jdbcUrl

        // execution
        val result = DriverTypeResolver.current()

        // validation
        assertEquals(expectedResult, result)
    }

    @Test
    fun `when JDBC URL is for a MySQL database, it should detect it correctly`() {
        // scenario
        val expectedResult = DriverType.MYSQL
        val jdbcUrl = "jdbc:mysql://${randomString()}/${randomString()}"
        every { EnvironmentVariables.jdbcUrl } returns jdbcUrl

        // execution
        val result = DriverTypeResolver.current()

        // validation
        assertEquals(expectedResult, result)
    }

    @Test
    fun `when JDBC URL is for a SQL Server database, it should detect it correctly`() {
        // scenario
        val expectedResult = DriverType.SQL_SERVER
        val jdbcUrl = "jdbc:sqlserver://${randomString()}/${randomString()}"
        every { EnvironmentVariables.jdbcUrl } returns jdbcUrl

        // execution
        val result = DriverTypeResolver.current()

        // validation
        assertEquals(expectedResult, result)
    }

    @Test
    fun `when JDBC URL is for an unknown database, it should throw an error`() {
        // scenario
        val database = randomString()
        val jdbcUrl = "jdbc:$database://${randomString()}/${randomString()}"
        every { EnvironmentVariables.jdbcUrl } returns jdbcUrl

        // validation
        expectedException.expectMatch<InvocationTargetException> { exception ->
            assertEquals(exception.targetException.message, "Unsupported database: $database")
        }

        // execution
        DriverTypeResolver.current()
    }
}