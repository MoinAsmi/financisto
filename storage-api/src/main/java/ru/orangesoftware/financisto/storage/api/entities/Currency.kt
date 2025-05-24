package ru.orangesoftware.financisto.storage.api.entities

data class Currency(
    val id: Long = 0,
    val title: String,
    val name: String,
    val symbol: String,
    val isDefault: Boolean,
    val decimals: Int,
    val decimalSeparator: String,
    val groupSeparator: String,
    val symbol1Position: Int,
    val symbol2Position: Int,
    val lastModified: Long = System.currentTimeMillis()
)
