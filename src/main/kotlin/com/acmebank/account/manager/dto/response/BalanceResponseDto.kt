package com.acmebank.account.manager.dto.response

import java.math.BigDecimal

data class BalanceResponseDto(var balance: BigDecimal, var currency: String)
