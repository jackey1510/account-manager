package com.acmebank.account.manager.dto.request

import java.math.BigDecimal
import java.util.UUID

data class TransactionRequestDto(
        var debtorAccountNumber: String,
        var creditorAccountNumber: String,
        var amount: BigDecimal,
        var currency: String = "HKD",
        var idempodencyKey: UUID
)
