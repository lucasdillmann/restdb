package br.com.dillmann.restdb.domain.data.getPage.jdbc

import java.net.URLDecoder

/**
 * Escapes current String to avoid SQL injections
 */
fun String.escapeSql() =
    URLDecoder.decode(this, Charsets.UTF_8).replace("'", "''")