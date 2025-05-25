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
        currencyId = currencyId,
        totalAmount = totalAmount,
        type = AccountType.valueOf(type.name),
        isActive = isActive,
        sortOrder = sortOrder,
        note = note,
        lastTransactionDate = lastTransactionDate,
        creationDate = creationDate,
        limitAmount = limitAmount,
        cardIssuer = cardIssuer?.let { issuer ->
            when (type) {
                StorageAccount.Type.CREDIT_CARD,
                StorageAccount.Type.DEBIT_CARD -> CardIssuer.valueOf(issuer).name
                StorageAccount.Type.ELECTRONIC -> ElectronicPaymentType.valueOf(issuer).name
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
        currencyId = currencyId,
        totalAmount = totalAmount,
        type = StorageAccount.Type.valueOf(type.name),
        isActive = isActive,
        sortOrder = sortOrder,
        note = note,
        lastTransactionDate = lastTransactionDate,
        creationDate = creationDate,
        limitAmount = limitAmount,
        cardIssuer = cardIssuer,
        issuer = issuer,
        number = number
    )
}
