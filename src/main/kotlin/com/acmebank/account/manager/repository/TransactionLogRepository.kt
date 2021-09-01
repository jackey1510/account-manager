package com.acmebank.account.manager.repository

import com.acmebank.account.manager.model.TransactionLog
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface TransactionLogRepository :
                JpaRepository<TransactionLog, UUID>, JpaSpecificationExecutor<TransactionLog> {}
