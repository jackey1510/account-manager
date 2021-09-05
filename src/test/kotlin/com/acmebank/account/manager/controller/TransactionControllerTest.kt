package com.acmebank.account.manager.controller

import com.acmebank.account.manager.dto.request.TransactionRequestDto
import com.acmebank.account.manager.dto.request.toTransaction
import com.acmebank.account.manager.enum.TransactionStatus
import com.acmebank.account.manager.error.handler.CurrencyNotSupportedException
import com.acmebank.account.manager.error.handler.InsufficientBalanceException
import com.acmebank.account.manager.service.TransactionService
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import java.util.*

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TransactionControllerTest {
    @InjectMocks
    private lateinit var transactionController: TransactionController

    @Mock
    private lateinit var transactionService: TransactionService

    @BeforeAll
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun contextLoads() {
    }

    @Test
    fun transferSuccess() {

        val transferRequest = TransactionRequestDto("12345678", "88888888", BigDecimal(100), "HKD", UUID.randomUUID())

        val mockTransaction = transferRequest.toTransaction()
        mockTransaction.id = UUID.randomUUID()
        var successTransaction = mockTransaction
        successTransaction.status = TransactionStatus.SUCCESS

        Mockito.`when`(transactionService.createTransaction(transferRequest)).thenReturn(mockTransaction)
        Mockito.`when`(transactionService.executeTransaction(mockTransaction)).thenReturn(successTransaction)

        val transactionResponse = transactionController.transferMoney(transferRequest)

        assert(transactionResponse.status == TransactionStatus.SUCCESS)
        assert(transactionResponse.amount.compareTo(transferRequest.amount) == 0)
        assert(transactionResponse.debtorAccountNumber == transferRequest.debtorAccountNumber)
        assert(transactionResponse.creditorAccountNumber == transferRequest.creditorAccountNumber)
        assert(transactionResponse.currency == "HKD")
    }

    @Test
    fun transferUsdFail() {
        val transferRequest = TransactionRequestDto("12345678", "88888888", BigDecimal(100), "USD", UUID.randomUUID())
        val mockTransaction = transferRequest.toTransaction()

        Mockito.`when`(transactionService.createTransaction(transferRequest)).thenReturn(mockTransaction)
        Mockito.`when`(transactionService.executeTransaction(mockTransaction))
            .thenThrow(CurrencyNotSupportedException(mockTransaction))

        assertThrows<CurrencyNotSupportedException> { transactionController.transferMoney(transferRequest) }
    }

    @Test
    fun insufficientBalanceFail() {
        val transferRequest =
            TransactionRequestDto("12345678", "88888888", BigDecimal(100000000), "USD", UUID.randomUUID())
        val mockTransaction = transferRequest.toTransaction()
        Mockito.`when`(transactionService.createTransaction(transferRequest)).thenReturn(mockTransaction)
        Mockito.`when`(transactionService.executeTransaction(mockTransaction))
            .thenThrow(InsufficientBalanceException(mockTransaction))

        assertThrows<InsufficientBalanceException> { transactionController.transferMoney(transferRequest) }
    }


}