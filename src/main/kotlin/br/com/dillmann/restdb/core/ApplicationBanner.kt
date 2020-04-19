package br.com.dillmann.restdb.core

/**
 * Application startup banner utility
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-04-19
 */
object ApplicationBanner {

    /**
     * Prints application banner to console (stdout)
     */
    fun print() {
        val banner = """
                           __      ____  
           ________  _____/ /_____/ / /_ 
          / ___/ _ \/ ___/ __/ __  / __ \
         / /  /  __(__  ) /_/ /_/ / /_/ /
        /_/   \___/____/\__/\__,_/_.___/ 
                                         
        """.trimIndent()

        println(banner)
    }
}