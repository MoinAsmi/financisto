package ru.orangesoftware.financisto.feature.account.create.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.orangesoftware.financisto.feature.account.create.presentation.CreateAccountViewModel

val createAccountModule = module {
    viewModel {
        CreateAccountViewModel(
            createAccount = get(),
            getCurrencies = get()
        )
    }
}
