package com.acmebank.account.manager.model

import com.acmebank.account.manager.enum.TransactionStatus
import java.math.BigDecimal
import java.util.Date
import java.util.UUID

data class Transaction(
        var id: UUID? = null,
        var idempodencyKey: String = "",
        var amount: BigDecimal = BigDecimal(0),
        var currency: String = "HKD",
        var debtorAccountNumber: String = "",
        var creditorAccountNumber: String = "",
        var status: TransactionStatus = TransactionStatus.PENDING,
        var createdAt: Date = Date(),
        var updatedAt: Date = Date()
)
