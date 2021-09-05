package com.acmebank.account.manager.error.handler

import com.acmebank.account.manager.model.Transaction
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class TransactionAlreadyProcessedException(
    transaction: Transaction,
    message: String? = "Transaction already processed"
) : TransactionException(transaction, message) {
}