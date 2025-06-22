package ru.orangesoftware.financisto.feature.account.create.presentation

import ru.orangesoftware.financisto.storage.api.entities.Currency

data class CreateAccountState(
    val title: String = "",
    val type: String = "CASH", // Default type
    val currency: Currency? = null,
    val initialBalance: String = "",
    val note: String = "",
    val includeInTotals: Boolean = true,
    val currencies: List<Currency> = emptyList(),
    val isLoading: Boolean = true,
    val error: CreateAccountError? = null,
    val isCreated: Boolean = false
)
