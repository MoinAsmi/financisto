package ru.orangesoftware.financisto.storage.impl.dao

import ru.orangesoftware.financisto.storage.api.dao.CurrencyDao
import ru.orangesoftware.financisto.storage.api.entities.Currency
import ru.orangesoftware.financisto.storage.impl.entities.CurrencyEntity

class CurrencyDaoImpl(
    private val currencyRoomDao: CurrencyRoomDao
) : CurrencyDao {
    override suspend fun getCurrency(id: Long): Currency? =
        currencyRoomDao.getCurrency(id)?.toDomain()

    override suspend fun getCurrencies(): List<Currency> =
        currencyRoomDao.getCurrencies().map { it.toDomain() }

    override suspend fun getDefaultCurrency(): Currency? =
        currencyRoomDao.getDefaultCurrency()?.toDomain()

    override suspend fun insertCurrency(currency: Currency): Long =
        currencyRoomDao.insertCurrency(CurrencyEntity.fromDomain(currency))

    override suspend fun updateCurrency(currency: Currency) {
        currencyRoomDao.updateCurrency(CurrencyEntity.fromDomain(currency))
    }

    override suspend fun deleteCurrency(id: Long) {
        currencyRoomDao.deleteCurrency(id)
    }
}
