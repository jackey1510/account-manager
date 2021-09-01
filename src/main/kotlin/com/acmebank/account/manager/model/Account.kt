package com.acmebank.account.manager.model

import com.acmebank.account.manager.dto.response.BalanceResponseDto
import java.math.BigDecimal
import java.util.Date
import javax.persistence.*

@Entity
data class Account(
                @Id var accountNumber: String = "",
                var balance: BigDecimal = BigDecimal(0),
                var currency: String = "HKD",
                var createdAt: Date = Date(),
                var updatedAt: Date = Date()
)

fun Account.toBalanceResponseDto() = BalanceResponseDto(balance, currency)
