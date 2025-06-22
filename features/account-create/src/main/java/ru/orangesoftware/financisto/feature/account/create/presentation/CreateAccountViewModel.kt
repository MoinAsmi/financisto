package ru.orangesoftware.financisto.feature.account.create.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.orangesoftware.financisto.storage.api.entities.Account
import ru.orangesoftware.financisto.storage.api.entities.Currency
import ru.orangesoftware.financisto.usecase.api.CreateAccountUseCase
import ru.orangesoftware.financisto.usecase.api.GetCurrenciesUseCase

class CreateAccountViewModel(
    private val createAccount: CreateAccountUseCase,
    private val getCurrencies: GetCurrenciesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CreateAccountState())
    val state: StateFlow<CreateAccountState> = _state.asStateFlow()

    init {
        loadCurrencies()
    }

    fun onEvent(event: CreateAccountEvent) {
        when (event) {
            is CreateAccountEvent.TitleChanged -> updateTitle(event.title)
            is CreateAccountEvent.TypeSelected -> updateType(event.type)
            is CreateAccountEvent.CurrencySelected -> updateCurrency(event.currency)
            is CreateAccountEvent.InitialBalanceChanged -> updateBalance(event.amount)
            is CreateAccountEvent.NoteChanged -> updateNote(event.note)
            is CreateAccountEvent.IncludeInTotalsChanged -> updateIncludeInTotals(event.include)
            CreateAccountEvent.Submit -> createNewAccount()
            CreateAccountEvent.ErrorDismissed -> dismissError()
        }
    }

    private fun loadCurrencies() {
        viewModelScope.launch {
            getCurrencies().fold(
                onSuccess = { data ->
                    _state.update {
                        it.copy(currencies = data, isLoading = false)
                    }
                },
                onFailure = { error ->
                    _state.update { it.copy(
                        error = CreateAccountError.LoadError(error.message ?: "Failed to load currencies"),
                        isLoading = false
                    )}
                }
            )
        }
    }

    private fun updateTitle(title: String) {
        _state.update { it.copy(title = title) }
    }

    private fun updateType(type: String) {
        _state.update { it.copy(type = type) }
    }

    private fun updateCurrency(currency: Currency) {
        _state.update { it.copy(currency = currency) }
    }

    private fun updateBalance(amount: String) {
        _state.update { it.copy(initialBalance = amount) }
    }

    private fun updateNote(note: String) {
        _state.update { it.copy(note = note) }
    }

    private fun updateIncludeInTotals(include: Boolean) {
        _state.update { it.copy(includeInTotals = include) }
    }

    private fun createNewAccount() {
        val currentState = state.value
        
        // Basic validation
        if (currentState.title.isBlank()) {
            _state.update { it.copy(error = CreateAccountError.ValidationError("Title is required")) }
            return
        }

        if (currentState.currency == null) {
            _state.update { it.copy(error = CreateAccountError.ValidationError("Currency is required")) }
            return
        }

        val initialBalance = currentState.initialBalance.toDoubleOrNull()
        if (currentState.initialBalance.isNotBlank() && initialBalance == null) {
            _state.update { it.copy(error = CreateAccountError.ValidationError("Invalid initial balance")) }
            return
        }

        viewModelScope.launch {
            val account = Account(
                id = 0L, // Will be assigned by DB
                title = currentState.title,
                type = currentState.type,
                currency = currentState.currency,
                totalAmount = (initialBalance ?: 0.0).toLong(),
                sortOrder = 0,
                isActive = true,
                isIncludeIntoTotals = currentState.includeInTotals,
                lastTransactionDate = null,
                lastReconciliationDate = null,
                closingDay = 0,
                paymentDay = 0,
                note = currentState.note.takeIf { it.isNotBlank() },
                limitAmount = 0L,
                cardIssuer = null,
                issuer = null,
                number = null,
                creationDate = System.currentTimeMillis()
            )

            createAccount(account).fold(
                onSuccess = {
                    _state.update { it.copy(isCreated = true) }
                },
                onFailure = { error ->
                    _state.update { it.copy(
                        error = CreateAccountError.CreateError(error.message ?: "Failed to create account")
                    )}
                }
            )
        }
    }

    private fun dismissError() {
        _state.update { it.copy(error = null) }
    }
}
