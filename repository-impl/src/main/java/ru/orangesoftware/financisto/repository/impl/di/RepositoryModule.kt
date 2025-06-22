/*
 * Copyright © Microsoft Corporation. All rights reserved.
 */
package ru.orangesoftware.financisto.repository.impl.di

import org.koin.dsl.module
import ru.orangesoftware.financisto.repository.api.AccountRepository
import ru.orangesoftware.financisto.repository.impl.AccountRepositoryImpl

val repositoryModule = module {
    single<AccountRepository> {
        AccountRepositoryImpl(accountDao = get())
    }
}