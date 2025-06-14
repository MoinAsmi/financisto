package ru.orangesoftware.financisto.storage.impl.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.orangesoftware.financisto.storage.impl.entities.AccountEntity
import ru.orangesoftware.financisto.storage.impl.entities.AccountWithCurrency

@Dao
interface AccountRoomDao {
    @Transaction
    @Query("""
        SELECT a.* FROM accounts a 
        ORDER BY a.sortOrder, a.title
    """)
    fun getAccounts(): Flow<List<AccountWithCurrency>>

    @Transaction
    @Query("""
        SELECT a.* FROM accounts a 
        WHERE a.isActive = :isActive 
        ORDER BY a.sortOrder, a.title
    """)
    fun getAccountsByStatus(isActive: Boolean): Flow<List<AccountWithCurrency>>

    @Transaction
    @Query("SELECT * FROM accounts WHERE id = :id")
    suspend fun getAccount(id: Long): AccountWithCurrency?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: AccountEntity): Long

    @Update
    suspend fun updateAccount(account: AccountEntity)

    @Delete
    suspend fun deleteAccount(account: AccountEntity)

    @Query("UPDATE accounts SET totalAmount = :totalAmount WHERE id = :accountId")
    suspend fun updateAccountBalance(accountId: Long, totalAmount: Long)
}
