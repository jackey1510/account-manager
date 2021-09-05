package com.acmebank.account.manager.repository

import com.acmebank.account.manager.model.TransactionLog
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TransactionLogRepository :
    JpaRepository<TransactionLog, UUID>, JpaSpecificationExecutor<TransactionLog>
