package com.acmebank.account.manager.error.handler

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
class CurrencyNotSupportedException(message: String? = "Only HKD transaction is supported") :
    RuntimeException(message) {
}