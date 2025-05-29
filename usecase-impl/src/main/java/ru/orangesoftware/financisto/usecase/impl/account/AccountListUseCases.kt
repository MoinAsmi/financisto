package ru.orangesoftware.financisto.usecase.impl.account

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ru.orangesoftware.financisto.repository.api.AccountRepository
import ru.orangesoftware.financisto.repository.api.RepositoryResult
import ru.orangesoftware.financisto.usecase.api.*
import ru.orangesoftware.financisto.usecase.api.model.*
import ru.orangesoftware.financisto.usecase.impl.BaseUseCase
import ru.orangesoftware.financisto.usecase.impl.mapper.toDomain
import java.math.BigDecimal

class GetAccountListUseCaseImpl(
    private val repository: AccountRepository
) : GetAccountListUseCase, BaseUseCase() {
    
    override fun invoke(filter: AccountFilter): Flow<List<AccountWithBalance>> =
        repository.getAccountsByStatus(filter.showInactive)
            .map { accounts -> accounts.map { it.toDomain() } }
            .catch { e -> throw mapException(e) }
}

class ToggleAccountStatusUseCaseImpl(
    private val repository: AccountRepository
) : ToggleAccountStatusUseCase, BaseUseCase() {

    override suspend fun invoke(accountId: Long): Result<Unit> = executeWithResult {
        when (val accountResult = repository.getAccount(accountId)) {
            is RepositoryResult.Success -> {
                val account = accountResult.data
                when (val saveResult = repository.saveAccount(account.copy(isActive = !account.isActive))) {
                    is RepositoryResult.Success -> Unit
                    is RepositoryResult.Error -> throw saveResult.exception
                }
            }
            is RepositoryResult.Error -> throw accountResult.exception
        }
    }
}

class DeleteAccountUseCaseImpl(
    private val repository: AccountRepository
) : DeleteAccountUseCase, BaseUseCase() {

    override suspend fun invoke(accountId: Long): Result<Unit> = executeWithResult {
        when (val result = repository.deleteAccount(accountId)) {
            is RepositoryResult.Success -> Unit
            is RepositoryResult.Error -> throw result.exception
        }
    }
}

class CalculateAccountTotalsUseCaseImpl(
    private val repository: AccountRepository
) : CalculateAccountTotalsUseCase, BaseUseCase() {

    override suspend fun invoke(): Result<AccountTotals> = executeWithResult {
        when (val result = repository.getAccounts().first()) {
            is RepositoryResult.Success -> {
                val accounts = result.data
                val totalsByCurrency = accounts
                    .groupBy { it.currency }
                    .mapValues { (currency, accountsForCurrency) ->
                        object : Money {
                            override val amount = accountsForCurrency
                                .map { it.totalAmount }
                                .fold(BigDecimal.ZERO) { acc, amount -> 
                                    acc.add(BigDecimal.valueOf(amount))
                                }
                            override val currency = currency.toDomain()
                        }
                    }

                AccountTotals(
                    byCurrency = totalsByCurrency,
                    inHomeCurrency = totalsByCurrency.values.first() // TODO: Implement proper home currency conversion
                )
            }
            is RepositoryResult.Error -> throw result.exception
        }
    }
}
