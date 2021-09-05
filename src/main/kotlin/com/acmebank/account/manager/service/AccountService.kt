package com.acmebank.account.manager.service

import com.acmebank.account.manager.model.Account
import com.acmebank.account.manager.model.Transaction


interface AccountService {
    fun getAccountByAccountNumber(accountNumber: String): Account?
    fun transferBalance(transaction: Transaction)
}