package ru.orangesoftware.financisto.storage.impl.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.orangesoftware.financisto.storage.impl.entities.CurrencyEntity

@Dao
interface CurrencyRoomDao {
    @Query("SELECT * FROM currencies WHERE id = :id")
    suspend fun getCurrency(id: Long): CurrencyEntity?

    @Query("SELECT * FROM currencies ORDER BY isDefault DESC, name ASC")
    suspend fun getCurrencies(): List<CurrencyEntity>

    @Query("SELECT * FROM currencies WHERE isDefault = 1 LIMIT 1")
    suspend fun getDefaultCurrency(): CurrencyEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrency(currency: CurrencyEntity): Long

    @Update
    suspend fun updateCurrency(currency: CurrencyEntity)

    @Query("DELETE FROM currencies WHERE id = :id AND NOT EXISTS (SELECT 1 FROM accounts WHERE currencyId = :id)")
    suspend fun deleteCurrency(id: Long): Int

    // To support Account relationships
    @Query("""
        SELECT c.* FROM currencies c 
        INNER JOIN accounts a ON c.id = a.currencyId 
        WHERE a.id = :accountId
    """)
    suspend fun getCurrencyForAccount(accountId: Long): CurrencyEntity?
}
