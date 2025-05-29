package ru.orangesoftware.financisto.usecase.api

import ru.orangesoftware.financisto.usecase.api.model.Currency
import ru.orangesoftware.financisto.usecase.api.model.Money
import java.time.LocalDate
import java.time.LocalDateTime

sealed class AccountUseCaseException : Exception() {
    // Account-related errors
    data class AccountNotFound(val accountId: Long) : AccountUseCaseException()
    data class HasTransactions(val accountId: Long) : AccountUseCaseException()
    data class InactiveAccount(val accountId: Long) : AccountUseCaseException()
    
    // Balance-related errors
    data class InsufficientBalance(val accountId: Long, val available: Money, val required: Money) : AccountUseCaseException()
    data class InvalidBalanceUpdate(val accountId: Long, val message: String) : AccountUseCaseException()
    
    // Transaction-related errors
    data class TransactionNotFound(val transactionId: Long) : AccountUseCaseException()
    data class InvalidTransactionAmount(val amount: Money) : AccountUseCaseException()
    data class TransactionDateInFuture(val date: LocalDateTime) : AccountUseCaseException()
    
    // Transfer-related errors
    data class SameAccountTransfer(val accountId: Long) : AccountUseCaseException()
    data class InvalidTransferAmount(val fromAmount: Money, val toAmount: Money) : AccountUseCaseException()
    data class IncompatibleCurrencies(val fromCurrency: Currency, val toCurrency: Currency) : AccountUseCaseException()
    
    // Purge-related errors
    data class PurgeFailure(val accountId: Long, val message: String) : AccountUseCaseException()
    data class InvalidPurgeDate(val date: LocalDate) : AccountUseCaseException()
}
