/*
 * Copyright © Microsoft Corporation. All rights reserved.
 */
package ru.orangesoftware.financisto.storage.impl.di

import org.koin.dsl.module
import ru.orangesoftware.financisto.storage.api.dao.AccountDao
import ru.orangesoftware.financisto.storage.api.dao.CurrencyDao
import ru.orangesoftware.financisto.storage.impl.dao.AccountDaoImpl
import ru.orangesoftware.financisto.storage.impl.dao.CurrencyDaoImpl
import ru.orangesoftware.financisto.storage.impl.db.FinancistoDatabase

val storageModule = module {
    single {
        FinancistoDatabase.provideDatabase(get())
    }
    single {
        FinancistoDatabase.provideAccountDao(get())
    }
    single {
        FinancistoDatabase.provideCurrencyDao(get())
    }
    single<AccountDao> {
        AccountDaoImpl(get())
    }
    single<CurrencyDao> {
        CurrencyDaoImpl(get())
    }
}