package com.acmebank.account.manager.service

import com.acmebank.account.manager.dto.request.TransactionRequestDto
import com.acmebank.account.manager.dto.request.toTransaction
import com.acmebank.account.manager.enum.TransactionStatus
import com.acmebank.account.manager.error.handler.CurrencyNotSupportedException
import com.acmebank.account.manager.error.handler.DuplicateTransactionException
import com.acmebank.account.manager.error.handler.InsufficientBalanceException
import com.acmebank.account.manager.model.Transaction
import com.acmebank.account.manager.model.TransactionLog
import com.acmebank.account.manager.repository.TransactionLogRepository
import com.acmebank.account.manager.repository.TransactionRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.sql.SQLException


@Service
class TransactionServiceImpl : TransactionService {
    @Autowired
    private lateinit var transactionRepository: TransactionRepository

    @Autowired
    private lateinit var transactionLogRepository: TransactionLogRepository

    @Autowired
    private lateinit var accountService: AccountService

    @Value("\${spring.datasource.url}")
    private lateinit var databaseUrl: String

    private val logger = LoggerFactory.getLogger(TransactionService::class.java)

    @Transactional
    override fun createTransaction(transactionRequestDto: TransactionRequestDto): Transaction {
        var transaction = transactionRequestDto.toTransaction()
        logger.info(transaction.toString())

        try {
            transaction = transactionRepository.saveAndFlush(transaction)
            logger.info(transaction.id.toString())
            return transaction
        } catch (ex: Exception) {
            logger.error(ex.message)
            throw DuplicateTransactionException()
        }

    }

    @Transactional
    override fun executeTransaction(transaction: Transaction): Transaction {
        if (transaction.currency != "HKD") {
            transactionUpdate(transaction, TransactionStatus.FAILED, "Only HKD is supported")
            throw CurrencyNotSupportedException()
        }
        try {
            accountService.transferBalance(transaction)
            return transactionUpdate(transaction, TransactionStatus.SUCCESS)

        } catch (ex: SQLException) {
            logger.error(ex.message)
            throw ex
        } catch (ex: InsufficientBalanceException) {
            transactionUpdate(transaction, TransactionStatus.FAILED, "Insufficient Balance")
            throw ex
        }
    }

    @Transactional
    private fun transactionUpdate(
        transaction: Transaction,
        status: TransactionStatus,
        reason: String? = null
    ): Transaction {

        val transactionLog = TransactionLog(
            status = status,
            transactionId = transaction.id!!,
            reason = reason
        )
        transactionLogRepository.save(transactionLog)
        transaction.status = status
        return transactionRepository.save(transaction)

    }
}