package com.acmebank.account.manager.controller

import com.acmebank.account.manager.dto.response.BalanceResponseDto
import com.acmebank.account.manager.error.handler.DataNotFoundException
import com.acmebank.account.manager.model.toBalanceResponseDto
import com.acmebank.account.manager.service.AccountService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/account")
class AccountController {
    @Autowired
    private lateinit var accountService: AccountService

    @GetMapping("/balance/{accountNumber}")
    fun getAccountBalance(@PathVariable accountNumber: String): BalanceResponseDto {

        var account = accountService.getAccountByAccountNumber(accountNumber)
            ?: throw DataNotFoundException(String.format("Account Number %s Not Found", accountNumber))
        return account.toBalanceResponseDto()

    }
}