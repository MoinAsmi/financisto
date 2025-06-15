package ru.orangesoftware.financisto.usecase.api

sealed class AccountUseCaseException : Exception() {
    data class AccountNotFound(val accountId: Long) : AccountUseCaseException()
    data class DeleteFailed(val accountId: Long, val causeReason: String) : AccountUseCaseException()
    data class BalanceUpdateFailed(val accountId: Long, val causeReason: String) : AccountUseCaseException()
    data class UnknownException(val underlyingException: Exception): AccountUseCaseException()
}
