package com.acmebank.account.manager.controller

import com.acmebank.account.manager.dto.request.TransactionRequestDto
import com.acmebank.account.manager.dto.response.TransactionResponseDto
import com.acmebank.account.manager.model.toTransactionResponseDto
import com.acmebank.account.manager.service.TransactionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/transaction")
class TransactionController {
    @Autowired
    private lateinit var transactionService: TransactionService

    @PostMapping("/transfer")
    fun transferMoney(@Valid @RequestBody() body: TransactionRequestDto): TransactionResponseDto {
        val transaction = transactionService.createTransaction(body)
        return transactionService.executeTransaction(transaction).toTransactionResponseDto()

    }
}