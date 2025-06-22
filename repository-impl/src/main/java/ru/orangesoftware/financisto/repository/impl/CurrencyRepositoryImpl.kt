package ru.orangesoftware.financisto.repository.impl

import kotlinx.coroutines.flow.flow
import ru.orangesoftware.financisto.repository.api.CurrencyRepository
import ru.orangesoftware.financisto.repository.api.RepositoryResult
import ru.orangesoftware.financisto.storage.api.dao.CurrencyDao
import ru.orangesoftware.financisto.storage.api.entities.Currency

class CurrencyRepositoryImpl(
    private val currencyDao: CurrencyDao
) : BaseRepository(), CurrencyRepository {
    override suspend fun getAllCurrencies(): RepositoryResult<List<Currency>> = execute {
        currencyDao.getCurrencies()
    }
}
