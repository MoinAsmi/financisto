/*
 * Copyright © Microsoft Corporation. All rights reserved.
 */
package ru.orangesoftware.financisto.modern

import ru.orangesoftware.financisto.feature.account.presentation.AccountListFragmentDirections
import ru.orangesoftware.financisto.navigation.AccountListNavigator

/**
 *
 */
class NavigationServiceImpl: AccountListNavigator {
    override fun toEditAccount(accountId: Long) =
        AccountListFragmentDirections.actionAccountListToEditAccount(accountId)

    override fun toTransactions(accountId: Long) = AccountListFragmentDirections.actionAccountListToTransactions(accountId)

    override fun toCreateAccount() = AccountListFragmentDirections.actionAccountListToCreateAccount()
}
