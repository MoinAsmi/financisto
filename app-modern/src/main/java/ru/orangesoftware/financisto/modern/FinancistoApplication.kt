package ru.orangesoftware.financisto.modern

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ru.orangesoftware.financisto.feature.account.di.accountListModule
import ru.orangesoftware.financisto.repository.impl.di.repositoryModule
import ru.orangesoftware.financisto.storage.impl.di.storageModule
import ru.orangesoftware.financisto.usecase.impl.di.usecaseModule

class FinancistoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidLogger()
            androidContext(this@FinancistoApplication)
            modules(
                listOf(
                    storageModule,
                    repositoryModule,
                    usecaseModule,
                    accountListModule
                )
            )
        }
    }
}
