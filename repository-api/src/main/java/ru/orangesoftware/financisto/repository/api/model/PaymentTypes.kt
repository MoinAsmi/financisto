package ru.orangesoftware.financisto.repository.api.model

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
