/*
 * Copyright © Microsoft Corporation. All rights reserved.
 */
package com.moin.financisto.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.moin.financisto.data.dao.*
import com.moin.financisto.data.entities.*

/**
 *
 */
@Database(
    entities = [
        Account::class,
        Expense::class,
        User::class,
        ExpenseType::class,
        ExpenseAttribute::class,
        ExpenseAttributeValue::class,
        RunningBalance::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(TypeConvertors::class)
abstract class FinancistoDatabase: RoomDatabase() {
    abstract val accountDao: AccountDao
    abstract val runningBalanceDao: RunningBalanceDao
    abstract val expenseDao: ExpenseDao
    abstract val expenseTypeDao: ExpenseTypeDao
    abstract val expenseAttributeDao: ExpenseAttributeDao
    abstract val expenseAttributeValueDao: ExpenseAttributeValueDao
    abstract val userDao: UserDao

    companion object {
        @Volatile
        private var runningBalanceDaoInstance: RunningBalanceDao? = null

        fun getRunningBalancesDao(context: Context): RunningBalanceDao {
            return runningBalanceDaoInstance ?: buildDatabase(context).runningBalanceDao.also {
                runningBalanceDaoInstance = it
            }
        }

        @Volatile
        private var expenseDaoInstance: ExpenseDao? = null
        fun getExpenseDao(context: Context): ExpenseDao {
            return expenseDaoInstance ?: buildDatabase(context).expenseDao.also {
                expenseDaoInstance = it
            }
        }

        @Volatile
        private var expenseTypeDaoInstance: ExpenseTypeDao? = null
        fun getExpenseTypeDao(context: Context): ExpenseTypeDao {
            return expenseTypeDaoInstance ?: buildDatabase(context).expenseTypeDao.also {
                expenseTypeDaoInstance = it
            }
        }

        @Volatile
        private var expenseAttributeDaoInstance: ExpenseAttributeDao? = null
        fun getExpenseAttributeDao(context: Context): ExpenseAttributeDao {
            return expenseAttributeDaoInstance ?: buildDatabase(context).expenseAttributeDao.also {
                expenseAttributeDaoInstance = it
            }
        }

        @Volatile
        private var expenseAttributeValueDaoInstance: ExpenseAttributeValueDao? = null
        fun getExpenseAttributeValueDao(context: Context): ExpenseAttributeValueDao {
            return expenseAttributeValueDaoInstance ?: buildDatabase(context).expenseAttributeValueDao.also {
                expenseAttributeValueDaoInstance = it
            }
        }

        @Volatile
        private var accountDaoInstance: AccountDao? = null
        fun getAccountDao(context: Context): AccountDao {
            return accountDaoInstance ?: buildDatabase(context).accountDao.also {
                accountDaoInstance = it
            }
        }

        @Volatile
        private var userDaoInstance: UserDao? = null
        fun getUserDao(context: Context): UserDao {
            return userDaoInstance ?: buildDatabase(context).userDao.also {
                userDaoInstance = it
            }
        }

        private fun buildDatabase(context: Context): FinancistoDatabase = Room.databaseBuilder(
            context.applicationContext,
            FinancistoDatabase::class.java,
            "financisto_db"
        ).fallbackToDestructiveMigration().build()

    }
}
