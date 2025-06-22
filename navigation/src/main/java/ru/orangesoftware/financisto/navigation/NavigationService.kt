/*
 * Copyright © Microsoft Corporation. All rights reserved.
 */
package ru.orangesoftware.financisto.navigation

import androidx.navigation.NavDirections

/**
 *
 */
interface AccountListNavigator {
    fun toEditAccount(accountId: Long): NavDirections
    fun toTransactions(accountId: Long): NavDirections
    fun toCreateAccount(): NavDirections
}
