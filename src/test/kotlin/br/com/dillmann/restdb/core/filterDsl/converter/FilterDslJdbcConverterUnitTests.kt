package br.com.dillmann.restdb.core.filterDsl.converter

import br.com.dillmann.restdb.core.filterDsl.FilterDslParser
import br.com.dillmann.restdb.core.filterDsl.jdbc.JdbcPredicate
import br.com.dillmann.restdb.testUtils.randomDouble
import br.com.dillmann.restdb.testUtils.randomString
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-02
 */
class FilterDslJdbcConverterUnitTests {

    private val treeNode: TreeNode = mockk(relaxed = true)
    private lateinit var converter: FilterDslJdbcConverter

    @Before
    fun setUp() {
        mockkObject(TreeNodeCompiler)
        mockkConstructor(ConversionContext::class)
        every { anyConstructed<ConversionContext>().startNode(any()) } just Runs
        every { anyConstructed<ConversionContext>().endNode() } just Runs
        every { anyConstructed<ConversionContext>().currentNode } returns treeNode

        converter = FilterDslJdbcConverter()
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `it should start a group node when parser starts a group`() {
        // execution
        converter.enterGroup(mockk())

        // validation
        verify { anyConstructed<ConversionContext>().startNode(NodeType.GROUP) }
    }

    @Test
    fun `it should close current node when parser exits a group`() {
        // execution
        converter.exitGroup(mockk())

        // validation
        verify { anyConstructed<ConversionContext>().endNode() }
    }

    @Test
    fun `it should start a filter node when parser starts an expression`() {
        // execution
        converter.enterExpression(mockk())

        // validation
        verify { anyConstructed<ConversionContext>().startNode(NodeType.FILTER) }
    }

    @Test
    fun `it should close current node when parser exits an expression`() {
        // execution
        converter.exitExpression(mockk())

        // validation
        verify { anyConstructed<ConversionContext>().endNode() }
    }

    @Test
    fun `it should start a logical operator node when parser exits a logical operator`() {
        // scenario
        val parserContext: FilterDslParser.LogicalOperatorContext = mockk()
        every { parserContext.text } returns randomString()

        // execution
        converter.exitLogicalOperator(parserContext)

        // validation
        verify { anyConstructed<ConversionContext>().startNode(NodeType.LOGICAL_OPERATOR) }
    }

    @Test
    fun `it should close current node when parser exits a logical operator`() {
        // scenario
        val parserContext: FilterDslParser.LogicalOperatorContext = mockk()
        every { parserContext.text } returns randomString()

        // execution
        converter.exitLogicalOperator(parserContext)

        // validation
        verify { anyConstructed<ConversionContext>().endNode() }
    }

    @Test
    fun `it should set logical operator type when parser exits a logical operator`() {
        // scenario
        val expectedValue = randomString()
        val parserContext = mockk<FilterDslParser.LogicalOperatorContext>()
        every { parserContext.text } returns expectedValue

        // execution
        converter.exitLogicalOperator(parserContext)

        // validation
        verify { treeNode.logicalOperator = expectedValue }
    }

    @Test
    fun `it should set column name type when parser exits a column name`() {
        // scenario
        val expectedValue = randomString()
        val parserContext = mockk<FilterDslParser.ColumnNameContext>()
        every { parserContext.text } returns expectedValue

        // execution
        converter.exitColumnName(parserContext)

        // validation
        verify { treeNode.columnName = expectedValue }
    }

    @Test
    fun `it should set operation type when parser exits an operation`() {
        // scenario
        val expectedValue = randomString()
        val parserContext = mockk<FilterDslParser.OperationContext>()
        every { parserContext.text } returns expectedValue

        // execution
        converter.exitOperation(parserContext)

        // validation
        verify { treeNode.operation = expectedValue }
    }

    @Test
    fun `it should clear parameters when parser enters parameters section`() {
        // scenario
        val parserContext = mockk<FilterDslParser.ParametersContext>()

        // execution
        converter.enterParameters(parserContext)

        // validation
        verify { treeNode.parameters = mutableListOf() }
    }

    @Test
    fun `it should add a numeric parameter as Double when parser exits a numeric parameter`() {
        // scenario
        val expectedValue = randomDouble()
        val parserContext = mockk<FilterDslParser.ParameterNumericValueContext>()
        every { parserContext.text } returns expectedValue.toString()
        every { treeNode.parameters } returns emptyList()

        // execution
        converter.exitParameterNumericValue(parserContext)

        // validation
        verify { treeNode.parameters = listOf(expectedValue) }
    }

    @Test
    fun `it should add a numeric parameter as String when parser exits a string parameter`() {
        // scenario
        val expectedValue = randomString()
        val parserContext = mockk<FilterDslParser.ParameterStringValueContext>()
        every { parserContext.text } returns expectedValue
        every { treeNode.parameters } returns emptyList()

        // execution
        converter.exitParameterStringValue(parserContext)

        // validation
        verify { treeNode.parameters = listOf(expectedValue) }
    }

    @Test
    fun `it should delegate root node compilation to TreeNodeCompiler`() {
        // scenario
        val expectedResult: JdbcPredicate = mockk()
        every { TreeNodeCompiler.compile(any()) } returns expectedResult

        // execution
        val result = converter.jdbcPredicate()

        // validation
        assertEquals(expectedResult, result)
    }
}