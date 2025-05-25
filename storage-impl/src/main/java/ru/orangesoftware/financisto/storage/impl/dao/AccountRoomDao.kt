package ru.orangesoftware.financisto.storage.impl.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.orangesoftware.financisto.storage.impl.entities.AccountEntity

@Dao
interface AccountRoomDao {
    @Transaction
    @Query("SELECT * FROM accounts ORDER BY sortOrder, title")
    fun getAccounts(): Flow<List<AccountEntity>>

    @Transaction
    @Query("SELECT * FROM accounts WHERE isActive = 1 ORDER BY sortOrder, title")
    fun getActiveAccounts(): Flow<List<AccountEntity>>

    @Transaction
    @Query("SELECT * FROM accounts WHERE id = :id")
    fun getAccountById(id: Long): Flow<AccountEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(account: AccountEntity): Long

    @Update
    suspend fun update(account: AccountEntity)

    @Query("DELETE FROM accounts WHERE id = :accountId")
    suspend fun delete(accountId: Long)

    @Query("UPDATE accounts SET totalAmount = :totalAmount WHERE id = :accountId")
    suspend fun updateAccountBalance(accountId: Long, totalAmount: Long)
}
