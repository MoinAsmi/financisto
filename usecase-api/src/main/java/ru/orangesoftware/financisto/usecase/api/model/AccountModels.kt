package ru.orangesoftware.financisto.usecase.api.model

import java.time.LocalDateTime

interface Money {
    val amount: java.math.BigDecimal
    val currency: Currency
}

interface Currency {
    val id: Long
    val name: String
    val symbol: String
    val decimalPlaces: Int
}

enum class TransactionType {
    EXPENSE,
    INCOME,
    TRANSFER
}

data class AccountWithBalance(
    val account: Account,
    val balance: Money,
    val limitInfo: CreditLimitInfo? = null
)

data class Account(
    val id: Long = 0,
    val title: String,
    val type: AccountType,
    val currency: Currency,
    val isActive: Boolean = true,
    val sortOrder: Int = 0,
    val note: String? = null,
    val lastTransactionDate: LocalDateTime? = null,
    val creationDate: LocalDateTime = LocalDateTime.now()
)

data class CreditLimitInfo(
    val limit: Money,
    val available: Money
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

data class AccountTotals(
    val byCurrency: Map<Currency, Money>,
    val inHomeCurrency: Money
)

data class AccountDetails(
    val account: Account,
    val balance: Money,
    val limitInfo: CreditLimitInfo? = null,
    val transactionsSummary: TransactionsSummary,
    val lastTransaction: Transaction?
)

data class TransactionsSummary(
    val totalTransactions: Int,
    val lastTransactionDate: LocalDateTime?,
    val oldestTransactionDate: LocalDateTime?,
    val income: Money,
    val expense: Money
)

data class Transaction(
    val id: Long = 0,
    val accountId: Long,
    val type: TransactionType,
    val amount: Money,
    val date: LocalDateTime,
    val note: String? = null
)

data class Transfer(
    val amount: Money,
    val fromAccountAmount: Money,
    val toAccountAmount: Money,
    val date: LocalDateTime,
    val note: String? = null
)
