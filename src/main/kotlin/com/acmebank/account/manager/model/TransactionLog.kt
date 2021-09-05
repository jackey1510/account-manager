package com.acmebank.account.manager.model

import com.acmebank.account.manager.enum.TransactionStatus
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class TransactionLog(
    @Id @GeneratedValue var id: UUID? = null,
    var transactionId: UUID = UUID.randomUUID(),
    var status: TransactionStatus = TransactionStatus.PENDING,
    var reason: String? = null,
    var timestamp: Date = Date(),
)
