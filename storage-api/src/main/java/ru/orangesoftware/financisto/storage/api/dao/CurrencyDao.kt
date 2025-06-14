package ru.orangesoftware.financisto.storage.api.dao

import ru.orangesoftware.financisto.storage.api.entities.Currency

interface CurrencyDao {
    suspend fun getCurrency(id: Long): Currency?
    suspend fun getCurrencies(): List<Currency>
    suspend fun getDefaultCurrency(): Currency?
    suspend fun insertCurrency(currency: Currency): Long
    suspend fun updateCurrency(currency: Currency)
    suspend fun deleteCurrency(id: Long)
}
