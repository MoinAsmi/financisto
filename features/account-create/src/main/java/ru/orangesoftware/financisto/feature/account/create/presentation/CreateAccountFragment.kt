package ru.orangesoftware.financisto.feature.account.create.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import ru.orangesoftware.financisto.feature.account.create.databinding.FragmentCreateAccountBinding
import ru.orangesoftware.financisto.storage.api.entities.Currency
import ru.orangesoftware.financisto.usecase.api.extensions.AccountType


class CreateAccountFragment : Fragment() {

    private var _binding: FragmentCreateAccountBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CreateAccountViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeState()
    }

    private fun setupViews() {
        with(binding) {
            // Setup account type dropdown
            val accountTypes = AccountType.values().map { it.name }
            val accountTypeAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                accountTypes
            )
            (accountTypeLayout.editText as? AutoCompleteTextView)?.setAdapter(accountTypeAdapter)
            (accountTypeLayout.editText as? AutoCompleteTextView)?.setText(AccountType.CASH.name, false)

            // Text change listeners
            titleInput.doAfterTextChanged { text ->
                viewModel.onEvent(CreateAccountEvent.TitleChanged(text.toString()))
            }

            accountTypeDropdown.setOnItemClickListener { _, _, position, _ ->
                viewModel.onEvent(CreateAccountEvent.TypeSelected(accountTypes[position]))
            }

            initialBalanceInput.doAfterTextChanged { text ->
                viewModel.onEvent(CreateAccountEvent.InitialBalanceChanged(text.toString()))
            }

            noteInput.doAfterTextChanged { text ->
                viewModel.onEvent(CreateAccountEvent.NoteChanged(text.toString()))
            }

            includeTotalsSwitch.setOnCheckedChangeListener { _, isChecked ->
                viewModel.onEvent(CreateAccountEvent.IncludeInTotalsChanged(isChecked))
            }

            createAccountFab.setOnClickListener {
                viewModel.onEvent(CreateAccountEvent.Submit)
            }

            // Navigation
            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    updateUiState(state)
                }
            }
        }
    }

    private fun updateUiState(state: CreateAccountState) {
        with(binding) {
            // Update currency dropdown when currencies are loaded
            if (state.currencies.isNotEmpty()) {
                setupCurrencyDropdown(state.currencies)
            }

            // Show errors if any
            state.error?.let { error ->
                val message = when (error) {
                    is CreateAccountError.ValidationError -> error.message
                    is CreateAccountError.CreateError -> error.message
                    is CreateAccountError.LoadError -> error.message
                }
                Snackbar.make(root, message, Snackbar.LENGTH_LONG).show()
                viewModel.onEvent(CreateAccountEvent.ErrorDismissed)
            }

            // Navigate back on successful creation
            if (state.isCreated) {
                findNavController().navigateUp()
            }
        }
    }

    private fun setupCurrencyDropdown(currencies: List<Currency>) {
        val currencyAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            currencies.map { it.name }
        )
        with(binding) {
            (currencyLayout.editText as? AutoCompleteTextView)?.setAdapter(currencyAdapter)
            currencyDropdown.setOnItemClickListener { _, _, position, _ ->
                viewModel.onEvent(CreateAccountEvent.CurrencySelected(currencies[position]))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
