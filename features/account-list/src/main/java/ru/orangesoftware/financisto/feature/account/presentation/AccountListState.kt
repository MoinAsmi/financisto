package ru.orangesoftware.financisto.feature.account.presentation

import ru.orangesoftware.financisto.storage.api.entities.Account

data class AccountListState(
    val accounts: List<AccountListItem> = emptyList(),
    val totals: AccountTotals = AccountTotals(),
    val isLoading: Boolean = false,
    val error: AccountListError? = null,
    val filterActive: Boolean = true
)

data class AccountListItem(
    val account: Account,
    val formattedBalance: String,
    val isExpanded: Boolean = false
)

data class AccountTotals(
    val totalInDefaultCurrency: String = "",
    val byAccount: Map<String, String> = emptyMap()
)

sealed class AccountListError {
    data class LoadError(val message: String) : AccountListError()
    data class DeleteError(val accountId: Long, val message: String) : AccountListError()
    data class BalanceError(val accountId: Long, val message: String) : AccountListError()
}
