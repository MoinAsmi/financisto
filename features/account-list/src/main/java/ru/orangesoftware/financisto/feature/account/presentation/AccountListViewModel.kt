package ru.orangesoftware.financisto.feature.account.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.orangesoftware.financisto.storage.api.entities.Account
import ru.orangesoftware.financisto.usecase.api.*

class AccountListViewModel(
    private val getAccountList: GetAccountListUseCase,
    private val toggleAccountStatus: ToggleAccountStatusUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val updateAccountBalance: UpdateAccountBalanceUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AccountListState())
    val state: StateFlow<AccountListState> = _state.asStateFlow()

    private val expandedAccounts = mutableSetOf<Long>()
    
    init {
        loadAccounts()
    }

    fun onEvent(event: AccountListEvent) {
        when (event) {
            is AccountListEvent.AccountSelected -> {
                // Navigation will be handled by fragment
            }
            is AccountListEvent.AccountDeleted -> deleteAccount(event.account)
            is AccountListEvent.BalanceUpdated -> updateBalance(event.id)
            is AccountListEvent.StatusToggled -> toggleStatus(event.id)
            is AccountListEvent.ToggleExpanded -> toggleExpanded(event.id)
            AccountListEvent.FilterToggled -> toggleFilter()
            AccountListEvent.ErrorDismissed -> clearError()
            AccountListEvent.Refresh -> loadAccounts()
        }
    }

    private fun loadAccounts() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            getAccountList(state.value.filterActive)
                .collect { result ->
                    result.fold(
                        onFailure = { error ->
                            _state.update { it.copy(
                                isLoading = false,
                                error = AccountListError.LoadError(error.message ?: "Failed to load accounts")
                            )}
                        },
                        onSuccess = { accounts ->
                            _state.update { it.copy(
                                isLoading = false,
                                accounts = accounts.map { account -> account.toListItem() }
                            )}
                        }
                    )
                }
        }
    }

    private fun toggleStatus(accountId: Long) {
        viewModelScope.launch {
            toggleAccountStatus(accountId).fold(
                onSuccess = { _ ->
                    loadAccounts() // Reload accounts to show updated status
                },
                onFailure = { error ->
                    _state.update { it.copy(
                        error = AccountListError.LoadError(error.message ?: "Failed to toggle account status")
                    )}
                }
            )
        }
    }

    private fun deleteAccount(account: Account) {
        viewModelScope.launch {
            deleteAccountUseCase(account).fold(
                onSuccess = { _ ->
                    loadAccounts() // Reload accounts to show updated status
                },
                onFailure = { error ->
                    _state.update { it.copy(
                        error = AccountListError.DeleteError(account.id, error.message ?: "Failed to delete account")
                    )}
                }
            )
        }
    }

    private fun updateBalance(accountId: Long) {
        // This will be implemented when we have the balance update dialog
    }

    private fun toggleExpanded(accountId: Long) {
        if (expandedAccounts.contains(accountId)) {
            expandedAccounts.remove(accountId)
        } else {
            expandedAccounts.add(accountId)
        }
        updateAccountExpansionStates()
    }

    private fun toggleFilter() {
        _state.update { it.copy(filterActive = !it.filterActive) }
        loadAccounts()
    }

    private fun clearError() {
        _state.update { it.copy(error = null) }
    }

    private fun updateAccountExpansionStates() {
        _state.update { state ->
            state.copy(
                accounts = state.accounts.map { item ->
                    item.copy(isExpanded = expandedAccounts.contains(item.account.id))
                }
            )
        }
    }

    private fun Account.toListItem() = AccountListItem(
        account = this,
        formattedBalance = formatBalance(totalAmount, currency),
        isExpanded = expandedAccounts.contains(id)
    )

    private fun formatBalance(amount: Long, currency: ru.orangesoftware.financisto.storage.api.entities.Currency?): String {
        // TODO: Implement proper balance formatting with currency symbol
        return "${amount/100.0} ${currency?.symbol ?: ""}"
    }
}
