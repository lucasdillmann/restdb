package br.com.dillmann.restdb.core.statusPages.utils

import br.com.dillmann.restdb.testUtils.randomString
import io.mockk.every
import io.mockk.mockk
import java.sql.SQLException

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-03
 */
fun randomSqlException(): SQLException =
    mockk {
        every { message } returns randomString()
        every { sqlState } returns randomString()
        every { nextException } returns null
    }