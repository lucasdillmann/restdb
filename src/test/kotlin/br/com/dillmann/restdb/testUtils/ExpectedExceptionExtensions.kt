package br.com.dillmann.restdb.testUtils

import org.hamcrest.CustomMatcher
import org.hamcrest.Matcher
import org.junit.rules.ExpectedException

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-02
 */
inline fun <reified T : Exception> ExpectedException.expect(
    message: String,
    noinline matcher: () -> Matcher<T> = ::emptyMatcher
) = expect(T::class.java, message, matcher)

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-02
 */
fun <T : Exception> ExpectedException.expect(
    exceptionClass: Class<T>,
    message: String,
    matcher: () -> Matcher<T> = ::emptyMatcher
) {
    expect(exceptionClass)
    expectMessage(message)
    expect(matcher())
}

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-05-09
 */
fun <T : Exception> ExpectedException.expectMatch(validationAction: (T) -> Unit = {}) {
    val matcher = encapsulatedMatcher(validationAction)
    expect(matcher)
}

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-02
 */
fun <T> emptyMatcher(): Matcher<T> =
    object: CustomMatcher<T>("NO-OP") {
        override fun matches(item: Any?) = true
    }

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-05-09
 */
private fun <T> encapsulatedMatcher(validationAction: (T) -> Unit): Matcher<T> =
    object: CustomMatcher<T>("") {
        override fun matches(item: Any?): Boolean {
            @Suppress("UNCHECKED_CAST")
            validationAction(item as T)
            return true
        }
    }