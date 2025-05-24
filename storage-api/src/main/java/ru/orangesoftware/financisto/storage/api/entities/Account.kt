package ru.orangesoftware.financisto.storage.api.entities

data class Account(
    val id: Long = 0,
    val title: String,
    val type: String,
    val currency: Currency,
    val totalAmount: Long,
    val sortOrder: Int,
    val isActive: Boolean,
    val isIncludeIntoTotals: Boolean,
    val lastTransactionDate: Long?,
    val lastReconciliationDate: Long?,
    val closingDay: Int,
    val paymentDay: Int,
    val note: String?,
    val limitAmount: Long,
    val cardIssuer: String?,
    val issuer: String?,
    val number: String?,
    val creationDate: Long,
    val lastModified: Long = System.currentTimeMillis()
)
