package ru.orangesoftware.financisto.usecase.impl.account

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.orangesoftware.financisto.repository.api.AccountRepository
import ru.orangesoftware.financisto.repository.api.CurrencyRepository
import ru.orangesoftware.financisto.repository.api.RepositoryResult
import ru.orangesoftware.financisto.storage.api.entities.Account
import ru.orangesoftware.financisto.storage.api.entities.Currency
import ru.orangesoftware.financisto.usecase.api.CreateAccountUseCase
import ru.orangesoftware.financisto.usecase.api.GetCurrenciesUseCase
import ru.orangesoftware.financisto.usecase.impl.BaseUseCase

class CreateAccountUseCaseImpl(
    private val accountRepository: AccountRepository
) : BaseUseCase(), CreateAccountUseCase {
    override suspend fun invoke(account: Account): Result<Unit> {
        return when (val result = accountRepository.saveAccount(account)) {
            is RepositoryResult.Success -> Result.success(Unit)
            is RepositoryResult.Error -> Result.failure(mapError(result.exception))
        }
    }
}

class GetCurrenciesUseCaseImpl(
    private val currencyRepository: CurrencyRepository
) : BaseUseCase(), GetCurrenciesUseCase {
    override suspend fun invoke(): Result<List<Currency>> {
        return when (val result = currencyRepository.getAllCurrencies()) {
            is RepositoryResult.Success -> Result.success(result.data)
            is RepositoryResult.Error -> Result.failure(mapError(result.exception))
        }
    }
}
