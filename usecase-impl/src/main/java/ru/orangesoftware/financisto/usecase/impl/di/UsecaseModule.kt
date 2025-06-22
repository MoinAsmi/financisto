/*
 * Copyright © Microsoft Corporation. All rights reserved.
 */
package ru.orangesoftware.financisto.usecase.impl.di

import org.koin.dsl.module
import ru.orangesoftware.financisto.usecase.api.*
import ru.orangesoftware.financisto.usecase.impl.account.*

val usecaseModule = module {
    single<GetAccountListUseCase> { GetAccountListUseCaseImpl(get()) }
    single<GetAccountUseCase> { GetAccountUseCaseImpl(get()) }
    single<UpdateAccountBalanceUseCase> { UpdateAccountBalanceUseCaseImpl(get()) }
    single<ToggleAccountStatusUseCase> { ToggleAccountStatusUseCaseImpl(get()) }
    single<DeleteAccountUseCase> { DeleteAccountUseCaseImpl(get()) }
    single<CreateAccountUseCase> { CreateAccountUseCaseImpl(get()) }
    single<GetCurrenciesUseCase> { GetCurrenciesUseCaseImpl(get()) }
}