package br.com.dillmann.restdb.domain.data

import br.com.dillmann.restdb.domain.data.delete.handleDelete
import br.com.dillmann.restdb.domain.data.get.page.handleGetPage
import br.com.dillmann.restdb.domain.data.get.singleRow.handleGetRow
import br.com.dillmann.restdb.domain.data.patch.handlePatch
import br.com.dillmann.restdb.domain.data.post.handlePost
import br.com.dillmann.restdb.domain.data.put.handlePut
import io.ktor.application.call
import io.ktor.routing.*

/**
 * Routes definition for data endpoints
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-28
 */
fun Routing.dataEndpoints() {
    route("/data/{partition}/{table}") {
        post {
            handlePost(call)
        }

        get {
            handleGetPage(call)
        }

        head {
            handleGetPage(call)
        }

        get("/{id}") {
            handleGetRow(call)
        }

        head("/{id}") {
            handleGetRow(call)
        }

        delete("/{id}") {
            handleDelete(call)
        }

        put("/{id}") {
            handlePut(call)
        }

        patch("/{id}") {
            handlePatch(call)
        }
    }
}

