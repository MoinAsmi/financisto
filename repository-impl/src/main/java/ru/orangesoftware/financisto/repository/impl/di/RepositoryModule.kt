/*
 * Copyright © Microsoft Corporation. All rights reserved.
 */
package ru.orangesoftware.financisto.repository.impl.di

import org.koin.dsl.module
import ru.orangesoftware.financisto.repository.api.AccountRepository
import ru.orangesoftware.financisto.repository.api.CurrencyRepository
import ru.orangesoftware.financisto.repository.impl.AccountRepositoryImpl
import ru.orangesoftware.financisto.repository.impl.CurrencyRepositoryImpl

val repositoryModule = module {
    single<AccountRepository> {
        AccountRepositoryImpl(accountDao = get())
    }
    single<CurrencyRepository> { CurrencyRepositoryImpl(get()) }
}