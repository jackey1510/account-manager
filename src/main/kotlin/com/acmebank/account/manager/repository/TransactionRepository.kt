package com.acmebank.account.manager.repository

import com.acmebank.account.manager.model.Transaction
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface TransactionRepository :
        JpaRepository<Transaction, UUID>, JpaSpecificationExecutor<Transaction> {}
