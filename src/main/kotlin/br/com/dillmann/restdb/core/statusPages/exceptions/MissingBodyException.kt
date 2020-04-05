package br.com.dillmann.restdb.core.statusPages.exceptions

/**
 * Specialization of [BadRequestException] for requests without body and one is required
 *
 * @author Lucas Dillmann
 * @since 1.0.0, 2020-03-27
 */
class MissingBodyException :
    BadRequestException("Request body is required and cannot be empty")