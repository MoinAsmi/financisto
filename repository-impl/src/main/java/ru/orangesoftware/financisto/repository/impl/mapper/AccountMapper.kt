package ru.orangesoftware.financisto.repository.impl.mapper

import ru.orangesoftware.financisto.repository.api.model.AccountType
import ru.orangesoftware.financisto.repository.api.model.CardIssuer
import ru.orangesoftware.financisto.repository.api.model.ElectronicPaymentType
import ru.orangesoftware.financisto.storage.api.entities.Account as StorageAccount


fun StorageAccount.accountType(): AccountType = AccountType.valueOf(type)
fun StorageAccount.cardIssuer(): CardIssuer? = if (accountType() == AccountType.DEBIT_CARD || accountType() == AccountType.CREDIT_CARD) {
    issuer?.let { CardIssuer.valueOf(it) }
} else {
    null
}

fun StorageAccount.electronicPaymentType() = if (accountType() == AccountType.ELECTRONIC_MONEY) issuer?.let {
    ElectronicPaymentType.valueOf(it)
} else null
