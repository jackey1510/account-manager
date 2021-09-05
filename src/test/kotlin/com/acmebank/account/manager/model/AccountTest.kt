package com.acmebank.account.manager.model

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountTest {
    @Test
    fun contextLoads() {
    }

    @Test
    fun createAccountObject() {
        val account = Account("12345678", currency = "HKD", balance = BigDecimal(100))
        assert(account.accountNumber == "12345678")
        assert(account.currency == "HKD")
        assert(account.balance.compareTo(BigDecimal(100)) == 0)
    }

    @Test
    fun toBalanceResponseDto() {
        val account = Account("12345678", currency = "HKD", balance = BigDecimal(100))
        val balanceResponseDto = account.toBalanceResponseDto()

        assert(balanceResponseDto.balance == account.balance)
        assert(balanceResponseDto.currency == account.currency)
    }

}