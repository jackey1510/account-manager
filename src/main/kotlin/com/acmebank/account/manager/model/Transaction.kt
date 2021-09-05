package com.acmebank.account.manager.model

import com.acmebank.account.manager.dto.response.TransactionResponseDto
import com.acmebank.account.manager.enum.TransactionStatus
import java.math.BigDecimal
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Transaction(
    @Id @GeneratedValue var id: UUID? = null,
    var idempotencyKey: UUID = UUID.randomUUID(),
    var amount: BigDecimal = BigDecimal(0),
    var currency: String = "HKD",
    var debtorAccountNumber: String = "",
    var creditorAccountNumber: String = "",
    var status: TransactionStatus = TransactionStatus.PENDING,
    var createdAt: Date = Date(),
    var updatedAt: Date = Date()
)

fun Transaction.toTransactionResponseDto() =
    TransactionResponseDto(
        debtorAccountNumber,
        creditorAccountNumber,
        amount,
        currency,
        status,
        id!!
    )
