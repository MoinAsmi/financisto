package ru.orangesoftware.financisto.repository.api

import ru.orangesoftware.financisto.storage.api.entities.Currency

interface CurrencyRepository {
    suspend fun getAllCurrencies(): RepositoryResult<List<Currency>>
}
