package ru.orangesoftware.financisto.feature.account.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.orangesoftware.financisto.feature.account.presentation.AccountListViewModel
import ru.orangesoftware.financisto.usecase.impl.account.*

val accountListModule = module {
    viewModel {
        AccountListViewModel(
            getAccountList = GetAccountListUseCaseImpl(get()),
            toggleAccountStatus = ToggleAccountStatusUseCaseImpl(get()),
            deleteAccountUseCase = DeleteAccountUseCaseImpl(get()),
            updateAccountBalance = UpdateAccountBalanceUseCaseImpl(get())
        )
    }
}
