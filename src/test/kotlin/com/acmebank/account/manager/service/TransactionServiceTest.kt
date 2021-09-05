package com.acmebank.account.manager.service

import com.acmebank.account.manager.dto.request.TransactionRequestDto
import com.acmebank.account.manager.dto.request.toTransaction
import com.acmebank.account.manager.enum.TransactionStatus
import com.acmebank.account.manager.error.handler.CurrencyNotSupportedException
import com.acmebank.account.manager.error.handler.DuplicateTransactionException
import com.acmebank.account.manager.error.handler.InsufficientBalanceException
import com.acmebank.account.manager.error.handler.TransactionAlreadyProcessedException
import com.acmebank.account.manager.model.Transaction
import com.acmebank.account.manager.repository.TransactionLogRepository
import com.acmebank.account.manager.repository.TransactionRepository
import org.junit.jupiter.api.*
import org.mockito.*
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import java.util.*

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TransactionServiceTest {
    @InjectMocks
    private lateinit var transactionServiceImpl: TransactionServiceImpl

    @Spy
    private lateinit var transactionRepository: TransactionRepository

    @Mock
    private lateinit var accountService: AccountService

    @Mock
    private lateinit var transactionLogRepository: TransactionLogRepository

    private lateinit var transferRequest: TransactionRequestDto
    private lateinit var mockTransaction: Transaction


    @BeforeAll
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @BeforeEach
    fun setMockDate() {
        transferRequest = TransactionRequestDto("12345678", "88888888", BigDecimal(100000000), "HKD", UUID.randomUUID())
        mockTransaction = transferRequest.toTransaction()
        mockTransaction.id = UUID.randomUUID()
    }

    @Test
    fun contextLoads() {
    }

    @Test
    fun createTransaction() {
        Mockito.`when`(transactionRepository.saveAndFlush(Mockito.any(Transaction::class.java)))
            .thenReturn(mockTransaction)
        val result = transactionServiceImpl.createTransaction(transferRequest)
        assert(result == mockTransaction)

    }

    @Test
    fun createDuplicateTransactionFail() {
        Mockito.`when`(transactionRepository.saveAndFlush(Mockito.any(Transaction::class.java)))
            .thenThrow(DuplicateTransactionException())

        assertThrows<DuplicateTransactionException> { transactionServiceImpl.createTransaction(transferRequest) }


    }

    @Test
    fun executeTransactionSuccess() {
        Mockito.`when`(accountService.transferBalance(mockTransaction)).thenAnswer { }
        val expected = mockTransaction.copy()
        expected.status = TransactionStatus.SUCCESS
        Mockito.`when`(transactionRepository.save(Mockito.any(Transaction::class.java))).thenReturn(expected)
        val result = transactionServiceImpl.executeTransaction(mockTransaction)

        assert(result == expected)
    }

    @Test
    fun executeTransactionFailWithWrongCurrency() {
        var usdTransaction = mockTransaction
        usdTransaction.currency = "USD"
        val expected = mockTransaction.copy()
        expected.status = TransactionStatus.FAILED
        Mockito.`when`(transactionRepository.save(Mockito.any(Transaction::class.java))).thenReturn(expected)
        assertThrows<CurrencyNotSupportedException> { transactionServiceImpl.executeTransaction(usdTransaction) }
    }

    @Test
    fun executeTransactionFailWithInsufficientBalance() {
        val expected = mockTransaction.copy()
        expected.status = TransactionStatus.FAILED
        Mockito.`when`(transactionRepository.save(Mockito.any(Transaction::class.java))).thenReturn(expected)
        Mockito.`when`(accountService.transferBalance(mockTransaction))
            .thenThrow(InsufficientBalanceException(expected))
        assertThrows<InsufficientBalanceException> { transactionServiceImpl.executeTransaction(mockTransaction) }
    }

    @Test
    fun transactionAlreadyProcessed() {
        val processedTransaction = mockTransaction.copy()
        processedTransaction.status = TransactionStatus.SUCCESS
        assertThrows<TransactionAlreadyProcessedException> {
            transactionServiceImpl.executeTransaction(
                processedTransaction
            )
        }
    }

}