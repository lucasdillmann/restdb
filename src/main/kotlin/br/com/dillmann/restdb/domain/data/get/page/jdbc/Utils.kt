package br.com.dillmann.restdb.domain.data.get.page.jdbc

import java.net.URLDecoder

/**
 * Escapes current String to avoid SQL injections
 */
fun String.escapeSql() =
    URLDecoder.decode(this, Charsets.UTF_8).replace("'", "''")