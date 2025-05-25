package ru.orangesoftware.financisto.repository.api

sealed class RepositoryResult<out T> {
    data class Success<T>(val data: T) : RepositoryResult<T>()
    data class Error(val exception: Exception) : RepositoryResult<Nothing>()
}

sealed class RepositoryException : Exception() {
    data class NotFound(override val message: String) : RepositoryException()
    data class ValidationError(override val message: String) : RepositoryException()
    data class DatabaseError(override val message: String, override val cause: Throwable? = null) : RepositoryException()
}
