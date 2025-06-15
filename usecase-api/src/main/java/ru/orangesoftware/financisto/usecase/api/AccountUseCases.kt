package ru.orangesoftware.financisto.usecase.api

import kotlinx.coroutines.flow.Flow
import ru.orangesoftware.financisto.storage.api.entities.Account

interface GetAccountListUseCase {
    operator fun invoke(includeInactive: Boolean = false): Flow<Result<List<Account>>>
}

interface GetAccountUseCase {
    suspend operator fun invoke(id: Long): Result<Account>
}

interface UpdateAccountBalanceUseCase {
    suspend operator fun invoke(id: Long, newBalance: Long): Result<Unit>
}

interface ToggleAccountStatusUseCase {
    suspend operator fun invoke(id: Long): Result<Unit>
}

interface DeleteAccountUseCase {
    suspend operator fun invoke(account: Account): Result<Unit>
}
