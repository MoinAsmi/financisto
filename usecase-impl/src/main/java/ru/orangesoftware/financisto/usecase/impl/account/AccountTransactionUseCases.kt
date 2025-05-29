package ru.orangesoftware.financisto.usecase.impl.account

import ru.orangesoftware.financisto.repository.api.AccountRepository
import ru.orangesoftware.financisto.repository.api.RepositoryResult
import ru.orangesoftware.financisto.usecase.api.AddTransactionUseCase
import ru.orangesoftware.financisto.usecase.api.AddTransferUseCase
import ru.orangesoftware.financisto.usecase.api.UpdateAccountBalanceUseCase
import ru.orangesoftware.financisto.usecase.api.PurgeAccountTransactionsUseCase
import ru.orangesoftware.financisto.usecase.api.model.Money
import ru.orangesoftware.financisto.usecase.api.model.Transaction
import ru.orangesoftware.financisto.usecase.api.model.Transfer
import ru.orangesoftware.financisto.usecase.impl.BaseUseCase
import ru.orangesoftware.financisto.usecase.impl.mapper.toStorage
import java.time.LocalDate

class AddTransactionUseCaseImpl(
    private val repository: AccountRepository
) : AddTransactionUseCase, BaseUseCase() {

    override suspend fun invoke(accountId: Long, transaction: Transaction): Result<Long> = executeWithResult {
        when (val result = repository.saveTransaction(transaction.toStorage())) {
            is RepositoryResult.Success -> result.data
            is RepositoryResult.Error -> throw result.exception
        }
    }
}

class AddTransferUseCaseImpl(
    private val repository: AccountRepository
) : AddTransferUseCase, BaseUseCase() {

    override suspend fun invoke(fromAccountId: Long, toAccountId: Long, transfer: Transfer): Result<Long> = executeWithResult {
        if (fromAccountId == toAccountId) {
            throw AccountUseCaseException.SameAccountTransfer(fromAccountId)
        }
        
        // Validate currencies match or can be converted
        when (val fromAccountResult = repository.getAccount(fromAccountId)) {
            is RepositoryResult.Success -> {
                when (val toAccountResult = repository.getAccount(toAccountId)) {
                    is RepositoryResult.Success -> {
                        if (fromAccountResult.data.currency != toAccountResult.data.currency) {
                            throw AccountUseCaseException.IncompatibleCurrencies(
                                fromAccountResult.data.currency,
                                toAccountResult.data.currency
                            )
                        }
                        
                        when (val result = repository.createTransfer(transfer.toStorage(fromAccountId, toAccountId))) {
                            is RepositoryResult.Success -> result.data
                            is RepositoryResult.Error -> throw result.exception
                        }
                    }
                    is RepositoryResult.Error -> throw toAccountResult.exception
                }
            }
            is RepositoryResult.Error -> throw fromAccountResult.exception
        }
    }
}

class UpdateAccountBalanceUseCaseImpl(
    private val repository: AccountRepository
) : UpdateAccountBalanceUseCase, BaseUseCase() {

    override suspend fun invoke(accountId: Long, newBalance: Money): Result<Unit> = executeWithResult {
        // First get current account details to check currency
        when (val accountResult = repository.getAccount(accountId)) {
            is RepositoryResult.Success -> {
                val account = accountResult.data
                if (account.currency != newBalance.currency) {
                    throw AccountUseCaseException.InvalidBalanceUpdate(accountId, 
                        "Balance update currency ${newBalance.currency.name} doesn't match account currency ${account.currency.name}")
                }
                
                when (val result = repository.updateAccountBalance(accountId, newBalance.amount.toLong())) {
                    is RepositoryResult.Success -> Unit
                    is RepositoryResult.Error -> throw result.exception
                }
            }
            is RepositoryResult.Error -> throw accountResult.exception
        }
    }
}

class PurgeAccountTransactionsUseCaseImpl(
    private val repository: AccountRepository
) : PurgeAccountTransactionsUseCase, BaseUseCase() {

    override suspend fun invoke(accountId: Long, beforeDate: LocalDate): Result<Int> = executeWithResult {
        if (beforeDate.isAfter(LocalDate.now())) {
            throw AccountUseCaseException.InvalidPurgeDate(beforeDate)
        }
        
        // First verify the account exists
        when (val accountResult = repository.getAccount(accountId)) {
            is RepositoryResult.Success -> {
                when (val result = repository.purgeAccountTransactions(accountId, beforeDate)) {
                    is RepositoryResult.Success -> result.data
                    is RepositoryResult.Error -> throw AccountUseCaseException.PurgeFailure(accountId, 
                        result.exception.message ?: "Unknown error during purge")
                }
            }
            is RepositoryResult.Error -> throw accountResult.exception
        }
    }
}
