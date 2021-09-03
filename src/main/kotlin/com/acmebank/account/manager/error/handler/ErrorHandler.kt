package com.acmebank.account.manager.error.handler

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.NoHandlerFoundException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class ErrorHandler : ResponseEntityExceptionHandler() {

    override fun handleNoHandlerFoundException(
        ex: NoHandlerFoundException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        logger.warn("handleNoHandlerFoundException: IN", ex)
        return buildResponseEntity(HttpStatus.NOT_FOUND)
    }

    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        logger.warn("handleHttpMessageNotReadable: IN", ex)
        return buildResponseEntity(HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(DataNotFoundException::class)
    fun handleDataNotFound(
        ex: DataNotFoundException
    ): ResponseEntity<Any> {
        logger.warn("handleDataNotFound: IN", ex)
        return buildResponseEntity(ex, HttpStatus.NOT_FOUND)
    }

    private fun buildResponseEntity(status: HttpStatus): ResponseEntity<Any> {
        return ResponseEntity(status)
    }

    private fun buildResponseEntity(ex: Exception, status: HttpStatus): ResponseEntity<Any> {
        val body = ErrorResponse(ex.message, status);
        return ResponseEntity(body, status)
    }

}