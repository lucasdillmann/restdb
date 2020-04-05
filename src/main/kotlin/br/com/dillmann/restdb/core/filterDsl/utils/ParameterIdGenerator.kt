package br.com.dillmann.restdb.core.filterDsl.utils

import java.util.*

/**
 * Produces an random parameter name
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-28
 */
fun randomParameterId() =
    "param_" + UUID.randomUUID().toString().split("-").last()