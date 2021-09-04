package com.acmebank.account.manager.model

import com.acmebank.account.manager.enum.TransactionStatus
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import java.util.*

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TransactionTest {
    @Test
    fun contextLoads() {
    }

    @Test
    fun createTransactionObject() {
        val transaction = Transaction(
            currency = "HKD",
            amount = BigDecimal(100),
            creditorAccountNumber = "12345678",
            debtorAccountNumber = "88888888",
            idempotencyKey = UUID.randomUUID()
        )
        assert(transaction.creditorAccountNumber == "12345678")
        assert(transaction.currency == "HKD")
        assert(transaction.debtorAccountNumber == "88888888")
        assert(transaction.status == TransactionStatus.PENDING)
        assert(transaction.amount.compareTo(BigDecimal(100)) == 0)
    }

    @Test
    fun toTransaction() {
        val transaction = Transaction(
            currency = "HKD",
            amount = BigDecimal(100),
            creditorAccountNumber = "12345678",
            debtorAccountNumber = "88888888",
            idempotencyKey = UUID.randomUUID(),
            id = UUID.randomUUID()
        )
        val transactionResponse = transaction.toTransactionResponseDto()

        assert(transactionResponse.amount == transaction.amount)
        assert(transactionResponse.currency == transaction.currency)
        assert(transactionResponse.creditorAccountNumber == transaction.creditorAccountNumber)
        assert(transactionResponse.debtorAccountNumber == transaction.debtorAccountNumber)
        assert(transactionResponse.status == transaction.status)
        assert(transactionResponse.id == transaction.id)
    }
}