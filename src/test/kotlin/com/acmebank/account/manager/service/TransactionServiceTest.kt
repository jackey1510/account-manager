package com.acmebank.account.manager.service

import com.acmebank.account.manager.dto.request.TransactionRequestDto
import com.acmebank.account.manager.dto.request.toTransaction
import com.acmebank.account.manager.enum.TransactionStatus
import com.acmebank.account.manager.error.handler.*
import com.acmebank.account.manager.model.Account
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
    private lateinit var transactionService: TransactionServiceImpl

    @Spy
    private lateinit var transactionRepository: TransactionRepository

    @Mock
    private lateinit var accountService: AccountService

    @Mock
    private lateinit var transactionLogRepository: TransactionLogRepository

    private lateinit var transferRequest: TransactionRequestDto
    private lateinit var mockTransaction: Transaction

    private lateinit var mockAccount: Account


    @BeforeAll
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @BeforeEach
    fun setMockDate() {
        transferRequest = TransactionRequestDto("12345678", "88888888", BigDecimal(100000000), "HKD", UUID.randomUUID())
        mockTransaction = transferRequest.toTransaction()
        mockTransaction.id = UUID.randomUUID()
        mockAccount = Account("12345678")
    }

    @Test
    fun contextLoads() {
    }

    @Test
    fun createTransaction() {
        Mockito.`when`(accountService.getAccountByAccountNumber(Mockito.anyString())).thenReturn(mockAccount)
        Mockito.`when`(transactionRepository.saveAndFlush(Mockito.any(Transaction::class.java)))
            .thenReturn(mockTransaction)
        val result = transactionService.createTransaction(transferRequest)
        assert(result == mockTransaction)
    }

    @Test
    fun createDuplicateTransactionFail() {
        Mockito.`when`(accountService.getAccountByAccountNumber(Mockito.anyString())).thenReturn(mockAccount)
        Mockito.`when`(transactionRepository.saveAndFlush(Mockito.any(Transaction::class.java)))
            .thenThrow(DuplicateTransactionException())
        assertThrows<DuplicateTransactionException> { transactionService.createTransaction(transferRequest) }
    }

    @Test
    fun createTransactionWithInvalidDebtorAccountNumber() {
        Mockito.`when`(accountService.getAccountByAccountNumber(mockTransaction.creditorAccountNumber))
            .thenReturn(mockAccount)
        Mockito.`when`(accountService.getAccountByAccountNumber(mockTransaction.debtorAccountNumber)).thenReturn(null)
        assertThrows<DataNotFoundException> { transactionService.createTransaction(transferRequest) }
    }

    @Test
    fun createTransactionWithInvalidCreditorAccountNumber() {
        Mockito.`when`(accountService.getAccountByAccountNumber(mockTransaction.debtorAccountNumber))
            .thenReturn(mockAccount)
        Mockito.`when`(accountService.getAccountByAccountNumber(mockTransaction.creditorAccountNumber)).thenReturn(null)
        assertThrows<DataNotFoundException> { transactionService.createTransaction(transferRequest) }
    }

    @Test
    fun createTransactionWithInvalidAmount() {
        Mockito.`when`(accountService.getAccountByAccountNumber(Mockito.anyString())).thenReturn(mockAccount)
        val invalidRequest = transferRequest.copy()
        invalidRequest.amount = BigDecimal(-1)
        assertThrows<InvalidTransferAmountException> { transactionService.createTransaction(invalidRequest) }
    }

    @Test
    fun createTransactionWithInvalidAccountNumber() {
        Mockito.`when`(accountService.getAccountByAccountNumber(Mockito.anyString())).thenReturn(mockAccount)
        val invalidRequest = transferRequest.copy()
        invalidRequest.debtorAccountNumber = invalidRequest.creditorAccountNumber
        assertThrows<InvalidAccountException> { transactionService.createTransaction(invalidRequest) }
    }

    @Test
    fun executeTransactionSuccess() {
        Mockito.`when`(accountService.transferBalance(mockTransaction)).thenAnswer { }
        val expected = mockTransaction.copy()
        expected.status = TransactionStatus.SUCCESS
        Mockito.`when`(transactionRepository.save(Mockito.any(Transaction::class.java))).thenReturn(expected)
        val result = transactionService.executeTransaction(mockTransaction)

        assert(result == expected)
    }

    @Test
    fun executeTransactionFailWithWrongCurrency() {
        var usdTransaction = mockTransaction
        usdTransaction.currency = "USD"
        val expected = mockTransaction.copy()
        expected.status = TransactionStatus.FAILED
        Mockito.`when`(transactionRepository.save(Mockito.any(Transaction::class.java))).thenReturn(expected)
        assertThrows<CurrencyNotSupportedException> { transactionService.executeTransaction(usdTransaction) }
    }

    @Test
    fun executeTransactionFailWithInsufficientBalance() {
        val expected = mockTransaction.copy()
        expected.status = TransactionStatus.FAILED
        Mockito.`when`(transactionRepository.save(Mockito.any(Transaction::class.java))).thenReturn(expected)
        Mockito.`when`(accountService.transferBalance(mockTransaction))
            .thenThrow(InsufficientBalanceException(expected))
        assertThrows<InsufficientBalanceException> { transactionService.executeTransaction(mockTransaction) }
    }

    @Test
    fun transactionAlreadyProcessed() {
        val processedTransaction = mockTransaction.copy()
        processedTransaction.status = TransactionStatus.SUCCESS
        assertThrows<TransactionAlreadyProcessedException> {
            transactionService.executeTransaction(
                processedTransaction
            )
        }
    }

}