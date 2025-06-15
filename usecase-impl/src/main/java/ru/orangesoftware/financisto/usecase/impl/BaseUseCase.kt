package ru.orangesoftware.financisto.usecase.impl

import ru.orangesoftware.financisto.repository.api.AccountRepository
import ru.orangesoftware.financisto.repository.api.RepositoryResult
import ru.orangesoftware.financisto.repository.api.RepositoryException
import ru.orangesoftware.financisto.usecase.api.*

abstract class BaseUseCase {
    protected fun mapError(error: Exception): AccountUseCaseException {
        val repositoryException = error as? RepositoryException
        return when(repositoryException) {
            is RepositoryException.NotFound -> AccountUseCaseException.AccountNotFound(0) // TODO: Extract ID
            is RepositoryException.ValidationError -> AccountUseCaseException.BalanceUpdateFailed(0, error.message ?: "Unknown error")
            is RepositoryException.DatabaseError -> AccountUseCaseException.DeleteFailed(0, error.message ?: "Database error")
            else -> AccountUseCaseException.UnknownException(error)
        }
    }
}
