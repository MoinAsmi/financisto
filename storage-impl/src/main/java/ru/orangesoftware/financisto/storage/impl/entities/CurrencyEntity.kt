package ru.orangesoftware.financisto.storage.impl.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.orangesoftware.financisto.storage.api.entities.Currency as CurrencyModel

@Entity(tableName = "currencies")
data class CurrencyEntity(
    @PrimaryKey val id: Long = 0,
    val title: String,
    val name: String,
    val symbol: String,
    val isDefault: Boolean,
    val decimals: Int,
    val decimalSeparator: String,
    val groupSeparator: String,
    val symbol1Position: Int,
    val symbol2Position: Int,
    val lastModified: Long
) {
    fun toDomain(): CurrencyModel = CurrencyModel(
        id = id,
        title = title,
        name = name,
        symbol = symbol,
        isDefault = isDefault,
        decimals = decimals,
        decimalSeparator = decimalSeparator,
        groupSeparator = groupSeparator,
        symbol1Position = symbol1Position,
        symbol2Position = symbol2Position,
        lastModified = lastModified
    )

    companion object {
        fun fromDomain(model: CurrencyModel) = CurrencyEntity(
            id = model.id,
            title = model.title,
            name = model.name,
            symbol = model.symbol,
            isDefault = model.isDefault,
            decimals = model.decimals,
            decimalSeparator = model.decimalSeparator,
            groupSeparator = model.groupSeparator,
            symbol1Position = model.symbol1Position,
            symbol2Position = model.symbol2Position,
            lastModified = model.lastModified
        )
    }
}
