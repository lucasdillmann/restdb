package br.com.dillmann.restdb.core.filterDsl

import br.com.dillmann.restdb.core.filterDsl.converter.FilterDslJdbcConverter
import br.com.dillmann.restdb.core.filterDsl.jdbc.JdbcPredicate
import br.com.dillmann.restdb.testUtils.randomString
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-03
 */
class FilterDslParserFacadeUnitTests {

    private val parser: FilterDslParser = mockk()

    @Before
    fun setUp() {
        mockkObject(FilterDslParserFactory)
        mockkConstructor(FilterDslJdbcConverter::class)

        every { parser.addParseListener(any()) } just Runs
        every { parser.root() } returns mockk()
        every { FilterDslParserFactory.build(any()) } returns parser
        every { anyConstructed<FilterDslJdbcConverter>().jdbcPredicate() } returns mockk()
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `it should use factory to get a parser instance`() {
        // scenario
        val input = randomString()

        // execution
        FilterDslParserFacade.parse(input, randomString(), randomString())

        // validation
        verify { FilterDslParserFactory.build(input) }
    }

    @Test
    fun `it should start parsing at root element`() {
        // execution
        FilterDslParserFacade.parse(randomString(), randomString(), randomString())

        // validation
        verify { parser.root() }
    }

    @Test
    fun `it should add conversion parser listener prior to starting the parsing`() {
        // execution
        FilterDslParserFacade.parse(randomString(), randomString(), randomString())

        // validation
        verifySequence {
            parser.addParseListener(any<FilterDslJdbcConverter>())
            parser.root()
        }
    }

    @Test
    fun `it should return converted predicate from delegate conversion listener`() {
        // scenario
        val expectedResult: JdbcPredicate = mockk()
        every { anyConstructed<FilterDslJdbcConverter>().jdbcPredicate() } returns expectedResult

        // execution
        val result = FilterDslParserFacade.parse(randomString(), randomString(), randomString())

        // validation
        assertEquals(expectedResult, result)
    }

}