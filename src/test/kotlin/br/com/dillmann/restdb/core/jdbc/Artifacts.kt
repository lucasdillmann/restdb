package br.com.dillmann.restdb.core.jdbc

import br.com.dillmann.restdb.domain.metadata.Product
import br.com.dillmann.restdb.domain.metadata.ProductsDetails
import br.com.dillmann.restdb.testUtils.randomString

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-03
 */
fun randomProductDetails() =
    ProductsDetails(randomProduct(), randomProduct())

/**
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-03
 */
fun randomProduct() =
    Product(randomString(), randomString())