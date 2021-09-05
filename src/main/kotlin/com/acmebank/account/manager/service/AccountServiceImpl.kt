package com.acmebank.account.manager.service

import com.acmebank.account.manager.error.handler.InsufficientBalanceException
import com.acmebank.account.manager.model.Account
import com.acmebank.account.manager.model.Transaction
import com.acmebank.account.manager.repository.AccountRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.LockModeType
import javax.persistence.PersistenceContext

@Service
class AccountServiceImpl : AccountService {
    private val logger = LoggerFactory.getLogger(AccountService::class.java)

    @Autowired
    private lateinit var accountRepository: AccountRepository

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    override fun getAccountByAccountNumber(accountNumber: String): Account? {
        return try {
            accountRepository.getAccountByAccountNumber(accountNumber)
        } catch (ex: RuntimeException) {
            logger.error(ex.message)
            return null
        }
    }

    @Transactional
    private fun getDebtorAccount(transaction: Transaction): Account {
        val properties = HashMap<String, Any>()
        properties["javax.persistence.lock.timeout"] = 2000L
        return entityManager.find(
            Account::class.java,
            transaction.debtorAccountNumber,
            LockModeType.PESSIMISTIC_READ,
            properties
        )

    }


    @Transactional
    private fun hasSufficientBalance(transaction: Transaction): Boolean {
        val debtorAccount = getDebtorAccount(transaction)
        // Insufficient Balance
        if (debtorAccount.balance.compareTo(transaction.amount) < 0) {
            return false
        }
        return true
    }

    @Transactional
    override fun transferBalance(transaction: Transaction) {
        if (!hasSufficientBalance(transaction)) {
            throw InsufficientBalanceException(transaction)
        }

        val creditorQuery =
            entityManager.createNativeQuery("UPDATE ACCOUNT SET BALANCE = BALANCE + ? WHERE ACCOUNT_NUMBER = ?")
        creditorQuery.setParameter(1, transaction.amount)
        creditorQuery.setParameter(2, transaction.creditorAccountNumber)


        val debtorQuery =
            entityManager.createNativeQuery("UPDATE ACCOUNT SET BALANCE = BALANCE - ? WHERE ACCOUNT_NUMBER = ?")
        debtorQuery.setParameter(1, transaction.amount)
        debtorQuery.setParameter(2, transaction.debtorAccountNumber)

        creditorQuery.executeUpdate()
        debtorQuery.executeUpdate()
    }
}