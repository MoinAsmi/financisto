package ru.orangesoftware.financisto.storage.impl.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import ru.orangesoftware.financisto.storage.api.entities.Account as AccountModel
import ru.orangesoftware.financisto.storage.api.entities.Currency as CurrencyModel

@Entity(
    tableName = "accounts",
    foreignKeys = [
        ForeignKey(
            entity = CurrencyEntity::class,
            parentColumns = ["id"],
            childColumns = ["currencyId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index("currencyId")
    ]
)
data class AccountEntity(
    @PrimaryKey val id: Long = 0,
    val title: String,
    val type: String,
    val currencyId: Long,
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
) {
    fun toDomain(currency: CurrencyModel): AccountModel = AccountModel(
        id = id,
        title = title,
        type = type,
        currency = currency,
        totalAmount = totalAmount,
        sortOrder = sortOrder,
        isActive = isActive,
        isIncludeIntoTotals = isIncludeIntoTotals,
        lastTransactionDate = lastTransactionDate,
        lastReconciliationDate = lastReconciliationDate,
        closingDay = closingDay,
        paymentDay = paymentDay,
        note = note,
        limitAmount = limitAmount,
        cardIssuer = cardIssuer,
        issuer = issuer,
        number = number,
        creationDate = creationDate,
        lastModified = lastModified
    )

    companion object {
        fun fromDomain(model: AccountModel) = AccountEntity(
            id = model.id,
            title = model.title,
            type = model.type,
            currencyId = model.currency.id,
            totalAmount = model.totalAmount,
            sortOrder = model.sortOrder,
            isActive = model.isActive,
            isIncludeIntoTotals = model.isIncludeIntoTotals,
            lastTransactionDate = model.lastTransactionDate,
            lastReconciliationDate = model.lastReconciliationDate,
            closingDay = model.closingDay,
            paymentDay = model.paymentDay,
            note = model.note,
            limitAmount = model.limitAmount,
            cardIssuer = model.cardIssuer,
            issuer = model.issuer,
            number = model.number,
            creationDate = model.creationDate,
            lastModified = model.lastModified
        )
    }
}
