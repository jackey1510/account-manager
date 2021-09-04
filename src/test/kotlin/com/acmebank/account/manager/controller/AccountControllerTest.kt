package com.acmebank.account.manager.controller

import com.acmebank.account.manager.error.handler.DataNotFoundException
import com.acmebank.account.manager.model.Account
import com.acmebank.account.manager.service.AccountService
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountControllerTest {

    @InjectMocks
    private lateinit var accountController: AccountController

    @Mock
    private lateinit var accountService: AccountService

    @BeforeAll
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun contextLoads() {
    }

    @Test
    fun getAccountBalance() {
        val accountNumber = "12345678"
        val balance = BigDecimal(1000)
        val account = Account(
            accountNumber,
            balance,
        )

//        val accountService = mock(AccountService::class.java)
        `when`(accountService.getAccountByAccountNumber(accountNumber)).thenReturn(account)

        val balanceResponseDto = accountController.getAccountBalance(accountNumber)
        assert(balanceResponseDto.balance == balance)
        assert(balanceResponseDto.currency == "HKD")

    }

    @Test
    fun getWrongAccount() {
        val accountNumber = "12345678"
        val accountService = mock(AccountService::class.java)
        `when`(accountService.getAccountByAccountNumber(accountNumber)).thenReturn(null)
        assertThrows<DataNotFoundException> {
            accountController.getAccountBalance(accountNumber)
        }
    }
}