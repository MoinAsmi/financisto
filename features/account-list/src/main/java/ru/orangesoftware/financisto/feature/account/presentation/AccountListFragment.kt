package ru.orangesoftware.financisto.feature.account.presentation

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import ru.orangesoftware.financisto.feature.account.R
import ru.orangesoftware.financisto.feature.account.databinding.FragmentAccountListBinding
import ru.orangesoftware.financisto.navigation.AccountListNavigator
import ru.orangesoftware.financisto.storage.api.entities.Account

class AccountListFragment : Fragment(R.layout.fragment_account_list) {
    private var _binding: FragmentAccountListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AccountListViewModel by inject()
    private val navigator: AccountListNavigator by inject()
    
    private val accountAdapter = AccountListAdapter(
        onItemClick = { account -> viewModel.onEvent(AccountListEvent.ToggleExpanded(account.id)) },
        onTransactionsClick = { account -> navigateToTransactions(account.id) },
        onEditClick = { account -> navigateToEditAccount(account.id) },
        onDeleteClick = { account -> confirmDelete(account) }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAccountListBinding.bind(view)

        setupViews()
        observeState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupViews() {
        with(binding) {
            accountList.adapter = accountAdapter

            swipeRefresh.setOnRefreshListener {
                viewModel.onEvent(AccountListEvent.Refresh)
            }

            addAccountFab.setOnClickListener {
                navigateToCreateAccount()
            }

            toolbar.setOnMenuItemClickListener { item ->
                when(item.itemId) {
                    R.id.action_filter -> {
                        viewModel.onEvent(AccountListEvent.FilterToggled)
                        true
                    }
                    R.id.action_refresh -> {
                        viewModel.onEvent(AccountListEvent.Refresh)
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    updateUiState(state)
                }
            }
        }
    }

    private fun updateUiState(state: AccountListState) {
        with(binding) {
            progressBar.isVisible = state.isLoading && accountAdapter.currentList.isEmpty()
            accountList.isVisible = !state.isLoading || accountAdapter.currentList.isNotEmpty()
            swipeRefresh.isRefreshing = state.isLoading && accountAdapter.currentList.isNotEmpty()
            
            accountAdapter.submitList(state.accounts)
            totalText.text = state.totals.totalInDefaultCurrency

            // Update menu items
            toolbar.menu.findItem(R.id.action_filter)?.isChecked = state.filterActive

            // Show error if any
            state.error?.let { error ->
                showError(error)
            }
        }
    }

    private fun showError(error: AccountListError) {
        val message = when(error) {
            is AccountListError.LoadError -> error.message
            is AccountListError.DeleteError -> getString(R.string.delete_account_error, error.message)
            is AccountListError.BalanceError -> getString(R.string.update_balance_error, error.message)
        }

        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
            .setAction(R.string.retry) {
                viewModel.onEvent(AccountListEvent.Refresh)
            }
            .show()

        viewModel.onEvent(AccountListEvent.ErrorDismissed)
    }

    private fun confirmDelete(account: Account) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.delete_account_title)
            .setMessage(R.string.delete_account_message)
            .setPositiveButton(R.string.delete) { _, _ ->
                viewModel.onEvent(AccountListEvent.AccountDeleted(account))
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun navigateToTransactions(accountId: Long) {
        findNavController().navigate(
            navigator.toTransactions(accountId)
        )
    }

    private fun navigateToEditAccount(accountId: Long) {
        findNavController().navigate(
            navigator.toEditAccount(accountId)
        )
    }

    private fun navigateToCreateAccount() {
        findNavController().navigate(
           navigator.toCreateAccount()
        )
    }
}
