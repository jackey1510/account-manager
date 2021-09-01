package com.acmebank.account.manager.repository

import com.acmebank.account.manager.model.Account
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository : JpaRepository<Account, UUID>, JpaSpecificationExecutor<Account> {
    fun getAccountByAccountNumber(accountNumber: String): Account
}
