package ru.orangesoftware.financisto.usecase.api.extensions

import ru.orangesoftware.financisto.storage.api.entities.Account

enum class AccountType {
    CASH,
    BANK,
    DEBIT_CARD,
    CREDIT_CARD,
    ELECTRONIC_MONEY,
    ASSET,
    LIABILITY;

    companion object {
        fun fromString(type: String): AccountType = valueOf(type)
    }
}

val Account.accountType: AccountType
    get() = AccountType.fromString(type)

val Account.isCard: Boolean
    get() = accountType == AccountType.CREDIT_CARD || accountType == AccountType.DEBIT_CARD

val Account.isElectronicMoney: Boolean
    get() = accountType == AccountType.ELECTRONIC_MONEY

val Account.hasIssuer: Boolean
    get() = isCard || isElectronicMoney
