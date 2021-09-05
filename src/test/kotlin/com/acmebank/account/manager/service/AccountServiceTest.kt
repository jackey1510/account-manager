package com.acmebank.account.manager.service

import com.acmebank.account.manager.enum.TransactionStatus
import com.acmebank.account.manager.error.handler.InsufficientBalanceException
import com.acmebank.account.manager.model.Account
import com.acmebank.account.manager.model.Transaction
import com.acmebank.account.manager.repository.AccountRepository
import org.junit.jupiter.api.*
import org.mockito.*
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.LockModeType
import javax.persistence.Query

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountServiceTest {
    @InjectMocks
    private lateinit var accountService: AccountServiceImpl

    @Spy
    private lateinit var accountRepository: AccountRepository

    @Spy
    private lateinit var entityManager: EntityManager

    @Mock
    private lateinit var mockQuery: Query

    private lateinit var mockAccount: Account

    @BeforeAll
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @BeforeEach
    fun mockDate() {
        mockAccount = Account("12345678", BigDecimal(1000000))
    }

    @Test
    fun contextLoads() {
    }

    @Test
    fun getAccountByAccountNumber() {
        Mockito.`when`(accountRepository.getAccountByAccountNumber(mockAccount.accountNumber)).thenReturn(mockAccount)
        val account = accountService.getAccountByAccountNumber(mockAccount.accountNumber)
        assert(account != null)
        assert(account!!.accountNumber == mockAccount.accountNumber)
        assert(account.balance.compareTo(mockAccount.balance) == 0)
        assert(account.currency == account.currency)
    }

    @Test
    fun getWrongAccountNumber() {
        val wrongNumber = "wrong"
        Mockito.`when`(accountRepository.getAccountByAccountNumber("wrong")).thenReturn(null)
        val account = accountService.getAccountByAccountNumber(wrongNumber)
        assert(account == null)
    }


    @Test
    fun insufficientBalanceForTransfer() {
        val transaction = Transaction(
            UUID.randomUUID(),
            UUID.randomUUID(),
            BigDecimal(1000),
            "HKD",
            "12345678",
            "88888888",
            TransactionStatus.PENDING
        )

        mockAccount.balance = BigDecimal(100)


        val properties = HashMap<String, Any>()
        properties["javax.persistence.lock.timeout"] = 2000L

        Mockito.`when`(
            entityManager.find(
                Account::class.java,
                transaction.debtorAccountNumber,
                LockModeType.PESSIMISTIC_READ,
                properties
            )
        ).thenReturn(mockAccount)


        assertThrows<InsufficientBalanceException> { accountService.transferBalance(transaction) }

    }

    @Test
    fun transferSuccess() {
        val transaction = Transaction(
            UUID.randomUUID(),
            UUID.randomUUID(),
            BigDecimal(1000),
            "HKD",
            "12345678",
            "88888888",
            TransactionStatus.PENDING
        )

        val properties = HashMap<String, Any>()
        properties["javax.persistence.lock.timeout"] = 2000L

        Mockito.`when`(
            entityManager.find(
                Account::class.java,
                transaction.debtorAccountNumber,
                LockModeType.PESSIMISTIC_READ,
                properties
            )
        ).thenReturn(mockAccount)

        Mockito.`when`(entityManager.createNativeQuery(Mockito.anyString()))
            .thenReturn(mockQuery)

        assertDoesNotThrow { accountService.transferBalance(transaction) }

    }


}