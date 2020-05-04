package br.com.dillmann.restdb.core.environment

import br.com.dillmann.restdb.testUtils.randomInt
import br.com.dillmann.restdb.testUtils.randomString
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-05-02
 */
class EnvironmentVariableResolverUnitTests {

    @Before
    fun setUp() {
        mockkObject(EnvironmentResolver)
        every { EnvironmentResolver.values() } returns emptyMap()
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `when asked for an environment value without a custom converter, it should provide one using string converter`() {
        // scenario
        val expectedKey = "RESTDB_TEST_RESULT" // should be code variable name but in snake case and with RESTDB prefix
        val expectedValue = randomString()
        every { EnvironmentResolver.values() } returns mapOf(expectedKey to expectedValue)

        // execution
        val testResult by env()

        // validation
        assertEquals(expectedValue, testResult)
    }

    @Test
    fun `when asked for an environment value with a custom converter, it should provide the converted value`() {
        // scenario
        val expectedKey = "RESTDB_TEST_RESULT" // should be code variable name but in snake case and with RESTDB prefix
        val expectedValue = randomInt()
        every { EnvironmentResolver.values() } returns mapOf(expectedKey to expectedValue.toString())

        // execution
        val testResult by env { it.toInt() }

        // validation
        assertEquals(expectedValue, testResult)
    }

    @Test
    fun `when asked for an environment value with a default value and no value set, it should return default value`() {
        // scenario
        val defaultValue = randomString()

        // execution
        val nonExistentEnvironmentVariable by env(defaultValue)

        // validation
        assertEquals(nonExistentEnvironmentVariable, defaultValue)
    }
}