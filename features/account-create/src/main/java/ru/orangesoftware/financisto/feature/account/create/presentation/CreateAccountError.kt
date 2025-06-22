package ru.orangesoftware.financisto.feature.account.create.presentation

sealed class CreateAccountError {
    data class ValidationError(val message: String) : CreateAccountError()
    data class CreateError(val message: String) : CreateAccountError()
    data class LoadError(val message: String) : CreateAccountError()
}
