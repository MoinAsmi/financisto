package ru.orangesoftware.financisto.usecase.api

import kotlinx.coroutines.flow.Flow
import ru.orangesoftware.financisto.storage.api.entities.Account
import ru.orangesoftware.financisto.storage.api.entities.Currency

interface CreateAccountUseCase {
    suspend operator fun invoke(account: Account): Result<Unit>
}

interface GetCurrenciesUseCase {
    suspend operator fun invoke(): Result<List<Currency>>
}
