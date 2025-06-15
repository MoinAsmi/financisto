package ru.orangesoftware.financisto.usecase.impl.account

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.orangesoftware.financisto.repository.api.AccountRepository
import ru.orangesoftware.financisto.repository.api.RepositoryException
import ru.orangesoftware.financisto.repository.api.RepositoryResult
import ru.orangesoftware.financisto.storage.api.entities.Account
import ru.orangesoftware.financisto.usecase.api.*
import ru.orangesoftware.financisto.usecase.impl.BaseUseCase

class GetAccountListUseCaseImpl(
    private val repository: AccountRepository
) : GetAccountListUseCase, BaseUseCase() {

    override fun invoke(includeInactive: Boolean): Flow<Result<List<Account>>> {

        val resultFlow = if (includeInactive) repository.getAccounts() else repository.getAccountsByStatus(true)
        return resultFlow.map { result ->
            when (result) {
                is RepositoryResult.Success -> Result.success(result.data)
                is RepositoryResult.Error -> Result.failure((result.exception as? RepositoryException)?.let {
                    mapError(
                        it
                    )
                } ?: AccountUseCaseException.UnknownException(result.exception))
            }
        }
    }
}

class GetAccountUseCaseImpl(
    private val repository: AccountRepository
) : GetAccountUseCase, BaseUseCase() {

    override suspend fun invoke(id: Long): Result<Account> =
        when (val result = repository.getAccount(id)) {
            is RepositoryResult.Success -> Result.success(result.data)
            is RepositoryResult.Error -> Result.failure(mapError(result.exception))
        }
}

class UpdateAccountBalanceUseCaseImpl(
    private val repository: AccountRepository
) : UpdateAccountBalanceUseCase, BaseUseCase() {

    override suspend fun invoke(id: Long, newBalance: Long): Result<Unit> =
        when (val result = repository.updateAccountBalance(id, newBalance)) {
            is RepositoryResult.Success -> Result.success(Unit)
            is RepositoryResult.Error -> Result.failure(mapError(result.exception))
        }
}

class ToggleAccountStatusUseCaseImpl(
    private val repository: AccountRepository
) : ToggleAccountStatusUseCase, BaseUseCase() {

    override suspend fun invoke(id: Long): Result<Unit> = 
        when (val accountResult = repository.getAccount(id)) {
            is RepositoryResult.Success -> {
                val account = accountResult.data
                when (val updateResult = repository.saveAccount(account.copy(isActive = !account.isActive))) {
                    is RepositoryResult.Success -> Result.success(Unit)
                    is RepositoryResult.Error -> Result.failure(mapError(updateResult.exception))
                }
            }
            is RepositoryResult.Error -> Result.failure(mapError(accountResult.exception))
        }
}

class DeleteAccountUseCaseImpl(
    private val repository: AccountRepository
) : DeleteAccountUseCase, BaseUseCase() {

    override suspend fun invoke(account: Account): Result<Unit> =
        when (val result = repository.deleteAccount(account)) {
            is RepositoryResult.Success -> Result.success(Unit)
            is RepositoryResult.Error -> Result.failure(mapError(result.exception))
        }
}
