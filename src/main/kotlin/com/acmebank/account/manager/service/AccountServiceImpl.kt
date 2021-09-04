package com.acmebank.account.manager.service

import com.acmebank.account.manager.model.Account
import com.acmebank.account.manager.repository.AccountRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AccountServiceImpl : AccountService {
    private val logger = LoggerFactory.getLogger(AccountService::class.java)

    @Autowired
    private lateinit var accountRepository: AccountRepository

    override fun getAccountByAccountNumber(accountNumber: String): Account? {
        return try {
            accountRepository.getAccountByAccountNumber(accountNumber)
        } catch (ex: RuntimeException) {
            logger.error(ex.message)
            return null
        }
    }
}