package com.acmebank.account.manager.service

import com.acmebank.account.manager.model.Account
import com.acmebank.account.manager.repository.AccountRepository
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.InjectMocks
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.Spy
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountServiceTest {
    @InjectMocks
    private lateinit var accountService: AccountServiceImpl;

    @Spy
    private lateinit var accountRepository: AccountRepository

    private val account1 = Account("12345678", BigDecimal(1000000))

    @BeforeAll
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun getAccountByAccountNumber() {
        Mockito.`when`(accountRepository.getAccountByAccountNumber(account1.accountNumber)).thenReturn(account1)
        val account = accountService.getAccountByAccountNumber(account1.accountNumber)
        assert(account != null)
        assert(account!!.accountNumber == account1.accountNumber)
        assert(account.balance.compareTo(account1.balance) == 0)
        assert(account.currency == account.currency)
    }

    @Test
    fun getWrongAccountNumber() {
        val wrongNumber = "wrong"
        val account = accountService.getAccountByAccountNumber(wrongNumber)
        assert(account == null)
    }


    @Test
    fun contextLoads() {
    }

}