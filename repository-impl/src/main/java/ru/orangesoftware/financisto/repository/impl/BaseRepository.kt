package ru.orangesoftware.financisto.repository.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ru.orangesoftware.financisto.repository.api.RepositoryException
import ru.orangesoftware.financisto.repository.api.RepositoryResult

abstract class BaseRepository {
    protected fun <T> executeFlow(block: suspend () -> Flow<T>): Flow<RepositoryResult<T>> = flow {
        try {
            block().collect { data ->
                emit(RepositoryResult.Success(data))
            }
        } catch (e: Exception) {
            emit(RepositoryResult.Error(mapException(e)))
        }
    }

    protected suspend fun <T> execute(block: suspend () -> T?): RepositoryResult<T> = try {
        block()?.let {
            RepositoryResult.Success(it)
        } ?: RepositoryResult.Error(Exception("Null result"))
    } catch (e: Exception) {
        RepositoryResult.Error(mapException(e))
    }

    private fun mapException(e: Exception): RepositoryException = when (e) {
        is IllegalArgumentException -> RepositoryException.ValidationError(e.message ?: "Validation failed")
        else -> RepositoryException.DatabaseError(e.message ?: "Database error", e)
    }
}
