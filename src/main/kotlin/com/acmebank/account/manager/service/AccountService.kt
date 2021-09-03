package com.acmebank.account.manager.service

import com.acmebank.account.manager.model.Account


interface AccountService {
    //    @Query("SELECT * FROM ACCOUNT WHERE account_number = ?1")
    fun getAccountByAccountNumber(accountNumber: String): Account?
}