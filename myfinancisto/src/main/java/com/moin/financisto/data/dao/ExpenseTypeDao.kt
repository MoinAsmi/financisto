/*
 * Copyright © Microsoft Corporation. All rights reserved.
 */
package com.moin.financisto.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.moin.financisto.data.entities.Expense
import com.moin.financisto.data.entities.ExpenseAttribute
import com.moin.financisto.data.entities.ExpenseAttributeValue
import com.moin.financisto.data.entities.ExpenseType

/**
 *
 */
@Dao
interface ExpenseTypeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpenseType(expenseType: ExpenseType)

    @Query("SELECT * FROM expense_types")
    suspend fun getAllExpenseTypes(): List<ExpenseType>
}

@Dao
interface ExpenseAttributeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpenseAttributeType(expenseAttribute: ExpenseAttribute)

    @Query("SELECT * FROM expense_attributes")
    suspend fun getAllExpenseAttributeTypes(): List<ExpenseAttribute>
}

@Dao
interface ExpenseAttributeValueDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpenseAttributeValue(expenseAttributeValue: ExpenseAttributeValue)

    @Query("SELECT * FROM expense_attribute_values")
    suspend fun getAllExpenseAttributeValues(): List<ExpenseAttributeValue>
}

@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense)

    @Query("SELECT * FROM expenses")
    suspend fun getAllExpenses(): List<Expense>
}
