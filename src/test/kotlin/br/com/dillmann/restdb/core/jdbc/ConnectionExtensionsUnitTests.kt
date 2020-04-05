package br.com.dillmann.restdb.core.jdbc

import io.mockk.*
import org.junit.Before
import org.junit.Test
import java.sql.Connection

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-03
 */
class ConnectionExtensionsUnitTests {

    private val connection: Connection = mockk()

    @Before
    fun setUp() {
        every { connection.beginRequest() } just Runs
        every { connection.commit() } just Runs
        every { connection.rollback() } just Runs
    }

    @Test
    fun `it should start transaction request`() {
        // execution
        connection.runInTransaction { }

        // validation
        verify { connection.beginRequest() }
    }

    @Test
    fun `it should commit transaction`() {
        // execution
        connection.runInTransaction { }

        // validation
        verify { connection.commit() }
    }

    @Test
    fun `it should rollback transaction when an exception is thrown`() {
        // execution
        runCatching {
            connection.runInTransaction { error("I'm sorry Dave, I'm afraid I can't do that") }
        }

        // validation
        verify { connection.rollback() }
    }

    @Test
    fun `it should not commit transaction when an exception is thrown`() {
        // execution
        runCatching {
            connection.runInTransaction { error("I'm sorry Dave, I'm afraid I can't do that") }
        }

        // validation
        verify(exactly = 0) { connection.commit() }
    }

    @Test
    fun `it should start transaction, execute action and commit in expected order`() {
        // scenario
        val action: () -> Unit = mockk(relaxed = true)

        // execution
        connection.runInTransaction(action)

        // validation
        verifyOrder {
            connection.beginRequest()
            action()
            connection.commit()
        }
    }

    @Test
    fun `it should start transaction, execute action and rollback in expected order`() {
        // scenario
        val action: () -> Unit = mockk()
        every { action.invoke() } throws IllegalStateException("I'm a teapot")

        // execution
        runCatching {
            connection.runInTransaction(action)
        }

        // validation
        verifyOrder {
            connection.beginRequest()
            action()
            connection.rollback()
        }
    }
}