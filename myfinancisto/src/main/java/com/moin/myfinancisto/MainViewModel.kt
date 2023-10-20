/*
 * Copyright © Microsoft Corporation. All rights reserved.
 */
package com.moin.myfinancisto

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moin.financisto.data.FinancistoDatabase
import kotlinx.coroutines.launch

/**
 *
 */
class MainViewModel : ViewModel() {
    val state = mutableStateOf(emptyList<String>())
    fun fetchItems(context: Context) {
        viewModelScope.launch {
            val dao = FinancistoDatabase.getRunningBalancesDao(context)
            val balance = dao.getRunningBalanceBeforeTime(1, System.currentTimeMillis())
            state.value = listOf(balance?.balance?.toString().orEmpty())
        }
    }
}
