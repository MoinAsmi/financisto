package ru.orangesoftware.financisto.repository.api.model

enum class AccountType {
    CASH,
    BANK,
    DEBIT_CARD,
    CREDIT_CARD,
    ELECTRONIC_MONEY,
    ASSET,
    LIABILITY
}

enum class CardIssuer(val iconId: Int) {
    VISA(0),        // We'll update these icon IDs from R.drawable
    MASTERCARD(0),
    AMEX(0),
    DISCOVER(0),
    MAESTRO(0),
    OTHER(0)
}

enum class ElectronicPaymentType(val iconId: Int) {
    PAYPAL(0),
    WEBMONEY(0),
    BITCOIN(0),
    OTHER(0)
}

enum class CurrencySymbolFormat {
    BEFORE_AMOUNT,
    AFTER_AMOUNT,
    BEFORE_AMOUNT_WITH_SPACE,
    AFTER_AMOUNT_WITH_SPACE
}
