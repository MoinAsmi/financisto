package ru.orangesoftware.financisto.repository.impl.mapper

import ru.orangesoftware.financisto.repository.api.model.Account
import ru.orangesoftware.financisto.repository.api.model.AccountType
import ru.orangesoftware.financisto.repository.api.model.CardIssuer
import ru.orangesoftware.financisto.repository.api.model.ElectronicPaymentType
import ru.orangesoftware.financisto.storage.api.entities.Account as StorageAccount

fun StorageAccount.toDomain(): Account {
    return Account(
        id = id,
        title = title,
        currencyId = currency?.id ?: -1,
        totalAmount = totalAmount,
        type = AccountType.valueOf(type),
        isActive = isActive,
        sortOrder = sortOrder,
        note = note,
        lastTransactionDate = lastTransactionDate,
        creationDate = creationDate,
        limitAmount = limitAmount,
        cardIssuer = cardIssuer?.let { issuer ->
            when (type) {
                AccountType.CREDIT_CARD.name,
                AccountType.DEBIT_CARD.name -> CardIssuer.valueOf(issuer).name
                AccountType.ELECTRONIC_MONEY.name -> ElectronicPaymentType.valueOf(issuer).name
                else -> issuer
            }
        },
        issuer = issuer,
        number = number
    )
}

fun Account.toStorage(): StorageAccount {
    return StorageAccount(
        id = id,
        title = title,
        currency = null,
        totalAmount = totalAmount,
        type = type.name,
        isActive = isActive,
        sortOrder = sortOrder,
        note = note,
        lastTransactionDate = lastTransactionDate,
        creationDate = creationDate,
        limitAmount = limitAmount,
        cardIssuer = cardIssuer,
        issuer = issuer,
        number = number,
        isIncludeIntoTotals = false,
        lastReconciliationDate = -1,
        closingDay = -1,
        paymentDay = -1,
        lastModified = -1
    )
}
