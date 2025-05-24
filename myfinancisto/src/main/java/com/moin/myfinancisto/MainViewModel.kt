/*
 * Copyright © Microsoft Corporation. All rights reserved.
 */
package com.moin.myfinancisto

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moin.financisto.data.FinancistoDatabase
import com.moin.financisto.data.entities.Account
import com.moin.financisto.data.entities.AccountType
import com.moin.financisto.data.entities.User
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 *
 */
class MainViewModel : ViewModel() {
    val state = mutableStateOf(emptyList<Account>())
    fun fetchItems(context: Context) {
        viewModelScope.launch {
            state.value = FinancistoDatabase.getAccountDao(context).getAllAccounts()
        }
    }

    private val random: Random = Random(23)

    fun createNewAccount(context: Context, account: Account) {
        viewModelScope.launch {
            val accountDao = FinancistoDatabase.getAccountDao(context)
            val userDao = FinancistoDatabase.getUserDao(context)
            userDao.insertUser(User(account.userId, "Moin", "soleh.firdous@gmail.com"))
            accountDao.insertAccount(account)//.copy(name = account.name + random.nextInt()))

            state.value = accountDao.getAllAccounts()
        }
    }
}
