package com.acmebank.account.manager.error.handler

import com.acmebank.account.manager.model.Transaction
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
abstract class TransactionException(transaction: Transaction, message: String? = "Transaction Error") :
    RuntimeException(message) {
    val transaction = transaction
}