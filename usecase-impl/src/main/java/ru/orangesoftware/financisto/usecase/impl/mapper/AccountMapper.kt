package ru.orangesoftware.financisto.usecase.impl.mapper

import ru.orangesoftware.financisto.repository.api.model.Account as RepoAccount
import ru.orangesoftware.financisto.repository.api.model.Currency as RepoCurrency
import ru.orangesoftware.financisto.usecase.api.model.*
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun RepoAccount.toDomain(): Account = Account(
    id = id,
    title = title,
    type = AccountType.valueOf(type.name),
    currency = currency.toDomain(),
    isActive = isActive,
    sortOrder = sortOrder,
    note = note,
    lastTransactionDate = lastTransactionDate?.let { 
        LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneId.systemDefault())
    },
    creationDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(creationDate), ZoneId.systemDefault())
)

fun RepoCurrency.toDomain(): Currency = object : Currency {
    override val id: Long = this@toDomain.id
    override val name: String = this@toDomain.name
    override val symbol: String = this@toDomain.symbol
    override val decimalPlaces: Int = this@toDomain.decimalPlaces
}

fun Long.toDomain(): Money = object : Money {
    override val amount: BigDecimal = BigDecimal.valueOf(this@toDomain)
    override val currency: Currency = TODO("Implement currency retrieval")
}

fun RepoAccount.toTransactionSummary(): TransactionsSummary = TransactionsSummary(
    totalTransactions = 0, // TODO: Implement from repository
    lastTransactionDate = lastTransactionDate?.let {
        LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneId.systemDefault())
    },
    oldestTransactionDate = null, // TODO: Implement from repository
    income = 0L.toDomain(),
    expense = 0L.toDomain()
)

fun Transaction.toStorage(): ru.orangesoftware.financisto.repository.api.model.Transaction = TODO()

fun Transfer.toStorage(fromAccountId: Long, toAccountId: Long): ru.orangesoftware.financisto.repository.api.model.Transfer = TODO()
