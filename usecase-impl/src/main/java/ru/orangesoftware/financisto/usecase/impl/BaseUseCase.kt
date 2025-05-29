package ru.orangesoftware.financisto.usecase.impl

import ru.orangesoftware.financisto.repository.api.RepositoryException
import ru.orangesoftware.financisto.usecase.api.AccountUseCaseException
import java.time.LocalDateTime

abstract class BaseUseCase {
    protected fun mapException(e: Exception): AccountUseCaseException {
        return when (e) {
            is RepositoryException -> when (e) {
                is RepositoryException.NotFound -> AccountUseCaseException.AccountNotFound(0) // TODO: Extract ID from message
                is RepositoryException.ValidationError -> when {
                    e.message?.contains("balance") == true -> AccountUseCaseException.InvalidBalanceUpdate(0, e.message)
                    e.message?.contains("transaction") == true -> AccountUseCaseException.TransactionNotFound(0)
                    else -> AccountUseCaseException.InvalidTransactionAmount(e.message ?: "Unknown validation error")
                }
                is RepositoryException.DatabaseError -> AccountUseCaseException.PurgeFailure(0, e.message ?: "Database error")
            }
            else -> AccountUseCaseException.PurgeFailure(0, "Unknown error: ${e.message}")
        }
    }

    protected suspend fun <T> executeWithResult(block: suspend () -> T): Result<T> = 
        try {
            Result.success(block())
        } catch (e: Exception) {
            Result.failure(mapException(e))
        }
}
