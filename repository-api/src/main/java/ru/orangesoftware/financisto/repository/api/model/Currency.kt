package ru.orangesoftware.financisto.repository.api.model

data class Currency(
    val id: Long = 0,
    val name: String,
    val symbol: String,
    val decimalPlaces: Int = 2,
    val symbolFormat: CurrencySymbolFormat = CurrencySymbolFormat.AFTER_AMOUNT,
    val isDefault: Boolean = false
)

enum class CurrencySymbolFormat {
    BEFORE_AMOUNT,
    AFTER_AMOUNT,
    BEFORE_AMOUNT_WITH_SPACE,
    AFTER_AMOUNT_WITH_SPACE
}
