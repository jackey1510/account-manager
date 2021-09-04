package com.acmebank.account.manager.service

import com.acmebank.account.manager.model.Account


interface AccountService {
    fun getAccountByAccountNumber(accountNumber: String): Account?
}