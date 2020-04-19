package br.com.dillmann.restdb.testUtils

import java.util.*
import kotlin.random.Random

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-02
 */
fun randomUuid(): UUID =
    UUID.randomUUID()

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-02
 */
fun randomString() =
    randomUuid().toString()

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-02
 */
fun randomDouble() =
    Random.nextDouble()

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-02
 */
fun randomInt() =
    Random.nextInt()

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-02
 */
fun randomLong() =
    Random.nextLong()

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-02
 */
fun randomPositiveInt() =
    randomInt().let { if (it < 0) it * -1 else it }