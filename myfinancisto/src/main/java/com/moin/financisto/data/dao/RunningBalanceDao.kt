/*
 * Copyright © Microsoft Corporation. All rights reserved.
 */
package com.moin.financisto.data.dao

import androidx.room.*
import com.moin.financisto.data.entities.RunningBalance

@Dao
interface RunningBalanceDao {
 @Query("SELECT * FROM running_balances WHERE accountId = :accountId AND date <= :dateTime ORDER BY date DESC LIMIT 1")
 suspend fun getRunningBalanceBeforeTime(accountId: Int, dateTime: Long): RunningBalance?

 @Query("SELECT * FROM running_balances WHERE accountId = :accountId AND date >= :dateTime ORDER BY date ASC LIMIT 1")
 suspend fun getRunningBalanceAfterTime(accountId: Int, dateTime: Long): RunningBalance?

 @Query("SELECT * FROM running_balances WHERE transactionId = :transactionId")
 suspend fun getRunningBalanceForTransaction(transactionId: Long): RunningBalance

 @Insert(onConflict = OnConflictStrategy.REPLACE)
 suspend fun insert(runningBalance: RunningBalance)

 @Delete
 suspend fun delete(runningBalance: RunningBalance)

 @Query("UPDATE running_balances SET balance = :balance WHERE transactionId = :transactionId AND accountId = :accountId")
 suspend fun updateBalanceForTransaction(transactionId: Long, accountId: Int, balance: Double)
}

