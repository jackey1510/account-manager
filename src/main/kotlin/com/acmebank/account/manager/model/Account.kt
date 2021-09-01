package com.acmebank.account.manager.model

import java.math.BigDecimal
import java.util.Date

data class Account(
        var accountNumber: String = "",
        var balance: BigDecimal = BigDecimal(0),
        var currency: String = "HKD",
        var createdAt: Date = Date(),
        var updatedAt: Date = Date()
)
