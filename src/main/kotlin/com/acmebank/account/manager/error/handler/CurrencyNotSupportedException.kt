package com.acmebank.account.manager.error.handler

import com.acmebank.account.manager.model.Transaction
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
class CurrencyNotSupportedException(transaction: Transaction, message: String? = "Only HKD transaction is supported") :
    TransactionException(transaction, message)