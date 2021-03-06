package com.acmebank.account.manager.dto.response

import com.acmebank.account.manager.enum.TransactionStatus
import java.math.BigDecimal
import java.util.*

data class TransactionResponseDto(
    var debtorAccountNumber: String,
    var creditorAccountNumber: String,
    var amount: BigDecimal,
    var currency: String,
    var status: TransactionStatus,
    var id: UUID
)
