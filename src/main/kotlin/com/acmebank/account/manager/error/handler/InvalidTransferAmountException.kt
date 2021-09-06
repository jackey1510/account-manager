package com.acmebank.account.manager.error.handler

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class InvalidTransferAmountException(message: String? = "Transfer amount must be greater than 0") :
    ValidationException(message) {
}