package br.com.dillmann.restdb.domain.data.exception

import br.com.dillmann.restdb.core.statusPages.exceptions.HttpException
import io.ktor.http.HttpStatusCode

/**
 * [HttpException] specialization for GET requests for rows of tables without primary key columns
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-29
 */
class NoPrimaryKeyException(schema: String, table: String) :
    HttpException(
        "Your request cannot be accepted. Table $table of schema $schema does not have a primary key.",
        HttpStatusCode.NotImplemented
    )