package com.acmebank.account.manager.service

import com.acmebank.account.manager.dto.request.TransactionRequestDto
import com.acmebank.account.manager.dto.request.toTransaction
import com.acmebank.account.manager.enum.TransactionStatus
import com.acmebank.account.manager.error.handler.*
import com.acmebank.account.manager.model.Transaction
import com.acmebank.account.manager.model.TransactionLog
import com.acmebank.account.manager.repository.TransactionLogRepository
import com.acmebank.account.manager.repository.TransactionRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.sql.SQLException


@Service
class TransactionServiceImpl : TransactionService {
    @Autowired
    private lateinit var transactionRepository: TransactionRepository

    @Autowired
    private lateinit var transactionLogRepository: TransactionLogRepository

    @Autowired
    private lateinit var accountService: AccountService

    private val logger = LoggerFactory.getLogger(TransactionService::class.java)

    @Transactional
    override fun createTransaction(transactionRequestDto: TransactionRequestDto): Transaction {
        var transaction = transactionRequestDto.toTransaction()
        validateTransaction(transaction)
        try {
            transaction = transactionRepository.saveAndFlush(transaction)
            logger.info(transaction.id.toString())
            return transaction
        } catch (ex: Exception) {
            logger.error(ex.message)
            throw DuplicateTransactionException()
        }

    }

    private fun validateTransaction(transaction: Transaction) {
        if (accountService.getAccountByAccountNumber(transaction.debtorAccountNumber) == null
        ) {
            throw DataNotFoundException(
                String.format(
                    "Debtor Account Number %s Not Found",
                    transaction.debtorAccountNumber
                )
            )
        }

        if (accountService.getAccountByAccountNumber(transaction.creditorAccountNumber) == null) {
            throw DataNotFoundException(
                String.format("Creditor Account Number %s Not Found", transaction.creditorAccountNumber)
            )
        }
        if (transaction.amount < BigDecimal.ZERO) {
            throw InvalidTransferAmountException()
        }
        if (transaction.debtorAccountNumber == transaction.creditorAccountNumber) {
            throw InvalidAccountException("Debtor Account Number and Creditor Account Number cannot be the same")
        }
    }

    @Transactional
    override fun executeTransaction(transaction: Transaction): Transaction {
        if (transaction.currency != "HKD") {
            transactionUpdate(transaction, TransactionStatus.FAILED, "Only HKD is supported")
            throw CurrencyNotSupportedException(transaction)
        }
        if (transaction.status != TransactionStatus.PENDING) {
            throw TransactionAlreadyProcessedException(transaction, "Transaction Processed")
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