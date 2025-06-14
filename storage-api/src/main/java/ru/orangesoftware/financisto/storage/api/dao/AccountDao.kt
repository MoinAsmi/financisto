package ru.orangesoftware.financisto.storage.api.dao

import kotlinx.coroutines.flow.Flow
import ru.orangesoftware.financisto.storage.api.entities.Account

interface AccountDao {
    fun getAccounts(): Flow<List<Account>>
    fun getAccountsByStatus(isActive: Boolean): Flow<List<Account>>
    suspend fun getAccount(id: Long): Account?
    suspend fun insertAccount(account: Account): Long
    suspend fun updateAccount(account: Account)
    suspend fun deleteAccount(account: Account)
    suspend fun updateAccountBalance(accountId: Long, totalAmount: Long)
}
