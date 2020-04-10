package br.com.dillmann.restdb.domain.data.exception

import br.com.dillmann.restdb.core.statusPages.exceptions.HttpException
import io.ktor.http.HttpStatusCode

/**
 * [HttpException] specialization for GET requests for rows of tables with composite primary keys
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-29
 */
class CompositePrimaryKeyException(partition: String, table: String, columns: Set<String>) :
    HttpException(
        "Your request cannot be accepted. Table $table of partition $partition has a composite primary key " +
                "(columns ${columns.joinToString()}), which is an unsupported scenario.",
        HttpStatusCode.NotImplemented
    )