package com.acmebank.account.manager.error.handler

import org.springframework.http.HttpStatus


data class ErrorResponse(val message: String?, val status: HttpStatus, val data: Any? = null)

