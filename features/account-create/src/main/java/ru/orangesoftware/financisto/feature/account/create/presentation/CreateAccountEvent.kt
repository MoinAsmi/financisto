package ru.orangesoftware.financisto.feature.account.create.presentation

import ru.orangesoftware.financisto.storage.api.entities.Currency

sealed class CreateAccountEvent {
    data class TitleChanged(val title: String) : CreateAccountEvent()
    data class TypeSelected(val type: String) : CreateAccountEvent()
    data class CurrencySelected(val currency: Currency) : CreateAccountEvent()
    data class InitialBalanceChanged(val amount: String) : CreateAccountEvent()
    data class NoteChanged(val note: String) : CreateAccountEvent()
    data class IncludeInTotalsChanged(val include: Boolean) : CreateAccountEvent()
    object Submit : CreateAccountEvent()
    object ErrorDismissed : CreateAccountEvent()
}
