package ru.orangesoftware.financisto.storage.api.dao

import kotlinx.coroutines.flow.Flow
import ru.orangesoftware.financisto.storage.api.entities.Account

interface AccountDao {
    fun getAccounts(): Flow<List<Account>>
    fun getActiveAccounts(): Flow<List<Account>>
    fun getAccountById(id: Long): Flow<Account?>
    suspend fun insert(account: Account): Long
    suspend fun update(account: Account)
    suspend fun delete(accountId: Long)
    suspend fun updateAccountBalance(accountId: Long, totalAmount: Long)
}
