package com.acmebank.account.manager.service

import com.acmebank.account.manager.dto.request.TransactionRequestDto
import com.acmebank.account.manager.model.Transaction


interface TransactionService {
    fun createTransaction(transactionRequestDto: TransactionRequestDto): Transaction
    fun executeTransaction(transaction: Transaction): Transaction
}