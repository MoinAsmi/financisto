package ru.orangesoftware.financisto.repository.api.model

data class Account(
    val id: Long = 0,
    val title: String,
    val currencyId: Long,
    val totalAmount: Long = 0,
    val type: AccountType,
    val isActive: Boolean = true,
    val sortOrder: Int = 0,
    val note: String? = null,
    val lastTransactionDate: Long? = null,
    val creationDate: Long = System.currentTimeMillis(),
    val limitAmount: Long = 0,
    val cardIssuer: String? = null,
    val issuer: String? = null,
    val number: String? = null
)

enum class AccountType {
    CASH,
    BANK,
    DEBIT_CARD,
    CREDIT_CARD,
    ELECTRONIC_MONEY,
    ASSET,
    LIABILITY
}
