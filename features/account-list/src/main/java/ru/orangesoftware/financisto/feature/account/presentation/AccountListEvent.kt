package ru.orangesoftware.financisto.feature.account.presentation

import ru.orangesoftware.financisto.storage.api.entities.Account

sealed class AccountListEvent {
    data class AccountSelected(val id: Long) : AccountListEvent()
    data class AccountDeleted(val account: Account) : AccountListEvent()
    data class BalanceUpdated(val id: Long) : AccountListEvent()
    data class ToggleExpanded(val id: Long) : AccountListEvent()
    data class StatusToggled(val id: Long) : AccountListEvent()
    object FilterToggled : AccountListEvent()
    object ErrorDismissed : AccountListEvent()
    object Refresh : AccountListEvent()
}
