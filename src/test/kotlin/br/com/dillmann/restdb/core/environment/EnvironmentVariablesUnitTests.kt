package br.com.dillmann.restdb.core.environment

import br.com.dillmann.restdb.testUtils.randomBoolean
import br.com.dillmann.restdb.testUtils.randomPositiveInt
import br.com.dillmann.restdb.testUtils.randomString
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert.assertThrows
import org.junit.Assume.assumeTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.slf4j.event.Level
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField
import kotlin.test.assertEquals

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-05-02
 */
@RunWith(Parameterized::class)
class EnvironmentVariablesUnitTests<T>(private val scenario: TestScenario<T>) {

    data class TestScenario<T>(
        val expectedValue: T,
        val defaultValue: T?,
        val variableName: String,
        val provider: EnvironmentVariables.() -> T
    ) {
        override fun toString() = variableName
    }

    companion object {

        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data(): Array<TestScenario<out Any>> =
            arrayOf(
                TestScenario(randomString(), null, "RESTDB_JDBC_URL") { jdbcUrl },
                TestScenario(randomString(), null, "RESTDB_JDBC_USERNAME") { jdbcUsername },
                TestScenario(randomString(), null, "RESTDB_JDBC_PASSWORD") { jdbcPassword },
                TestScenario(randomPositiveInt(), 4, "RESTDB_JDBC_MAXIMUM_CONNECTIONS") { jdbcMaximumConnections },
                TestScenario(randomPositiveInt(), 8080, "RESTDB_SERVER_PORT") { serverPort },
                TestScenario(randomString(), "0.0.0.0", "RESTDB_SERVER_HOST") { serverHost },
                TestScenario(randomBoolean(), false, "RESTDB_ENABLE_REQUEST_TRACING") { enableRequestTracing },
                TestScenario(randomBoolean(), true, "RESTDB_ENABLE_CORS") { enableCors },
                TestScenario(Level.values().random().toString(), "INFO", "RESTDB_LOG_LEVEL") { logLevel },
                TestScenario(randomString(), "development", "RESTDB_APPLICATION_VERSION") { applicationVersion }
            )
    }

    @Before
    fun setUp() {
        mockkObject(EnvironmentResolver)

        // Resets all cached values on delegated EnvironmentVariableResolver
        EnvironmentVariables::class.memberProperties.forEach { field ->
            val resolver = field
                .also { it.isAccessible = true }
                .getDelegate(EnvironmentVariables)
            EnvironmentVariableResolver::class
                .memberProperties
                .first { it.name == "value" }
                .javaField!!
                .also { it.isAccessible = true }
                .set(resolver, null)
        }
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `it should return expected value when environment variable exists`() {
        // scenario
        every { EnvironmentResolver.values() } returns mapOf(scenario.variableName to scenario.expectedValue.toString())
        val (expectedValue, _, _, provider) = scenario

        // execution
        val result = EnvironmentVariables.provider()

        // validation
        assertEquals(expectedValue, result)
    }

    @Test
    fun `it should return default value when environment value do not exists and default value is set`() {
        // conditional execution
        assumeTrue(scenario.defaultValue != null)

        // scenario
        every { EnvironmentResolver.values() } returns emptyMap()
        val (_, defaultValue, _, provider) = scenario

        // execution
        val result = EnvironmentVariables.provider()

        // validation
        assertEquals(defaultValue, result)
    }

    @Test
    fun `it should throw an error when environment value do not exists and no default value is set`() {
        // conditional execution
        assumeTrue(scenario.defaultValue == null)

        // scenario
        every { EnvironmentResolver.values() } returns emptyMap()
        val (_, _, variableName, provider) = scenario


        // execution and validation
        assertThrows("Missing environment variable: $variableName", IllegalStateException::class.java) {
            EnvironmentVariables.provider()
        }
    }

}