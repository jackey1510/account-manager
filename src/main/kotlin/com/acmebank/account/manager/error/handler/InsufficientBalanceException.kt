package com.acmebank.account.manager.error.handler

import com.acmebank.account.manager.model.Transaction
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
class InsufficientBalanceException(transaction: Transaction, message: String? = "Insufficient Balance") :
    TransactionException(transaction, message)