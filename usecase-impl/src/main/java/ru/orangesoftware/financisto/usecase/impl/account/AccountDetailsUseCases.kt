package ru.orangesoftware.financisto.usecase.impl.account

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import ru.orangesoftware.financisto.repository.api.AccountRepository
import ru.orangesoftware.financisto.repository.api.RepositoryResult
import ru.orangesoftware.financisto.usecase.api.GetAccountDetailsUseCase
import ru.orangesoftware.financisto.usecase.api.GetAccountTransactionsUseCase
import ru.orangesoftware.financisto.usecase.api.model.AccountDetails
import ru.orangesoftware.financisto.usecase.api.model.Transaction
import ru.orangesoftware.financisto.usecase.api.model.TransactionFilter
import ru.orangesoftware.financisto.usecase.impl.BaseUseCase
import ru.orangesoftware.financisto.usecase.impl.mapper.toDomain
import ru.orangesoftware.financisto.usecase.impl.mapper.toTransactionSummary

class GetAccountDetailsUseCaseImpl(
    private val repository: AccountRepository
) : GetAccountDetailsUseCase, BaseUseCase() {

    override suspend fun invoke(accountId: Long): Result<AccountDetails> = executeWithResult {
        when (val result = repository.getAccount(accountId)) {
            is RepositoryResult.Success -> {
                val account = result.data
                AccountDetails(
                    account = account.toDomain(),
                    balance = account.totalAmount.toDomain(),
                    limitInfo = account.limitAmount.toDomain(),
                    transactionsSummary = account.toTransactionSummary(),
                    lastTransaction = null // TODO: Implement last transaction retrieval
                )
            }
            is RepositoryResult.Error -> throw result.exception
        }
    }
}

class GetAccountTransactionsUseCaseImpl(
    private val repository: AccountRepository
) : GetAccountTransactionsUseCase, BaseUseCase() {

    override fun invoke(accountId: Long, filter: TransactionFilter): Flow<List<Transaction>> =
        repository.getAccountTransactions(accountId).map { result ->
            when (result) {
                is RepositoryResult.Success -> result.data.map { it.toDomain() }
                is RepositoryResult.Error -> throw mapException(result.exception)
            }
        }
}
