package com.acmebank.account.manager.model

import com.acmebank.account.manager.enum.TransactionStatus
import java.util.Date
import java.util.UUID

data class TransactionLog(
                var id: UUID? = null,
                var tranasctionId: UUID = UUID.randomUUID(),
                var status: TransactionStatus = TransactionStatus.PENDING,
                var timestamp: Date = Date(),
)
