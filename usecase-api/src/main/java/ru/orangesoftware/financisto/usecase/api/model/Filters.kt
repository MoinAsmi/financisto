package ru.orangesoftware.financisto.usecase.api.model

import java.time.LocalDateTime

data class AccountFilter(
    val showInactive: Boolean = false,
    val type: AccountType? = null,
    val sortOrder: AccountSortOrder = AccountSortOrder.BY_NAME
)

enum class AccountSortOrder {
    BY_NAME,
    BY_BALANCE,
    BY_LAST_TRANSACTION
}

data class TransactionFilter(
    val fromDate: LocalDateTime? = null,
    val toDate: LocalDateTime? = null,
    val type: TransactionType? = null,
    val minAmount: Money? = null,
    val maxAmount: Money? = null,
    val sortOrder: TransactionSortOrder = TransactionSortOrder.DATE_DESC
)

enum class TransactionSortOrder {
    DATE_ASC,
    DATE_DESC,
    AMOUNT_ASC,
    AMOUNT_DESC
}
