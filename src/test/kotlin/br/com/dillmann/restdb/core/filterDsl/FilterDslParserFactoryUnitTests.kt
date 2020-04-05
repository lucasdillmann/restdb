package br.com.dillmann.restdb.core.filterDsl

import br.com.dillmann.restdb.testUtils.randomString
import org.junit.Test
import kotlin.test.assertTrue

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-03
 */
class FilterDslParserFactoryUnitTests {

    @Test
    fun `produced parser should have default error listener attached`() {
        // execution
        val result = FilterDslParserFactory.build(randomString())

        // validation
        assertTrue { FilterDslErrorListener in result.errorListeners }
    }

    @Test
    fun `produced parser should have no parsing listeners attached`() {
        // execution
        val result = FilterDslParserFactory.build(randomString())

        // validation
        assertTrue { result.parseListeners.isEmpty() }
    }
}