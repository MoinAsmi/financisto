package ru.orangesoftware.financisto.storage.impl.entities

import androidx.room.Embedded
import androidx.room.Relation
import ru.orangesoftware.financisto.storage.api.entities.Account as AccountModel

data class AccountWithCurrency(
    @Embedded
    val account: AccountEntity,

    @Relation(
        parentColumn = "currencyId",
        entityColumn = "id"
    )
    val currency: CurrencyEntity?
) {
    fun toDomain(): AccountModel = account.toDomain(currency?.toDomain())
}
