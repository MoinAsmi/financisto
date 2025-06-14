package ru.orangesoftware.financisto.storage.impl.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.orangesoftware.financisto.storage.impl.dao.AccountRoomDao
import ru.orangesoftware.financisto.storage.impl.dao.CurrencyRoomDao
import ru.orangesoftware.financisto.storage.impl.entities.AccountEntity
import ru.orangesoftware.financisto.storage.impl.entities.CurrencyEntity

@Database(
    entities = [
        AccountEntity::class,
        CurrencyEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class FinancistoDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountRoomDao
    abstract fun currencyDao(): CurrencyRoomDao

    companion object {
        private const val DATABASE_NAME = "financisto_modern.db"

        fun create(context: Context): FinancistoDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                FinancistoDatabase::class.java,
                DATABASE_NAME
            ).build()
        }
    }
}
