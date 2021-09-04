package com.acmebank.account.manager.dto.request

import com.acmebank.account.manager.model.Transaction
import java.math.BigDecimal
import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

data class TransactionRequestDto(
    @field:NotNull
    var debtorAccountNumber: String,
    @field:NotNull
    var creditorAccountNumber: String,
    @field:Positive
    var amount: BigDecimal,
    @field:NotNull
    var currency: String = "HKD",
    @field:NotNull
    var idempotencyKey: UUID
)

fun TransactionRequestDto.toTransaction() = Transaction(
    idempotencyKey = idempotencyKey,
    debtorAccountNumber = debtorAccountNumber,
    creditorAccountNumber = creditorAccountNumber,
    amount = amount,
    currency = currency
)
