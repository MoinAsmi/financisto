package ru.orangesoftware.financisto.feature.account.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.orangesoftware.financisto.feature.account.R
import ru.orangesoftware.financisto.feature.account.databinding.ItemAccountBinding
import ru.orangesoftware.financisto.storage.api.entities.Account
import ru.orangesoftware.financisto.usecase.api.extensions.AccountType
import ru.orangesoftware.financisto.usecase.api.extensions.accountType
import ru.orangesoftware.financisto.usecase.api.extensions.isCard
import ru.orangesoftware.financisto.usecase.api.extensions.isElectronicMoney

class AccountListAdapter(
    private val onItemClick: (Account) -> Unit,
    private val onTransactionsClick: (Account) -> Unit,
    private val onEditClick: (Account) -> Unit,
    private val onDeleteClick: (Account) -> Unit
) : ListAdapter<AccountListItem, AccountListAdapter.AccountViewHolder>(AccountDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val binding = ItemAccountBinding.inflate(
            LayoutInflater.from(parent.context), 
            parent, 
            false
        )
        return AccountViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AccountViewHolder(
        private val binding: ItemAccountBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                getItem(bindingAdapterPosition)?.let { item ->
                    onItemClick(item.account)
                }
            }

            binding.transactionsButton.setOnClickListener {
                getItem(bindingAdapterPosition)?.let { item ->
                    onTransactionsClick(item.account)
                }
            }

            binding.editButton.setOnClickListener {
                getItem(bindingAdapterPosition)?.let { item ->
                    onEditClick(item.account)
                }
            }

            binding.deleteButton.setOnClickListener {
                getItem(bindingAdapterPosition)?.let { item ->
                    onDeleteClick(item.account)
                }
            }
        }

        fun bind(item: AccountListItem) {
            with(binding) {
                // Title and balance
                accountTitle.text = item.account.title
                accountBalance.text = item.formattedBalance
                accountType.text = item.account.accountType.name
                
                // Account icon based on type
                accountIcon.setImageResource(getAccountIcon(item.account))
                
                // Expand/Collapse actions
                val expanded = item.isExpanded
                transactionsButton.visibility = if (expanded) View.VISIBLE else View.GONE
                editButton.visibility = if (expanded) View.VISIBLE else View.GONE
                deleteButton.visibility = if (expanded) View.VISIBLE else View.GONE

                // Active/Inactive state
                val alpha = if (item.account.isActive) 1.0f else 0.5f
                root.alpha = alpha
                accountIcon.alpha = alpha
                accountTitle.alpha = alpha
                accountBalance.alpha = alpha
                accountType.alpha = alpha
            }
        }

        private fun getAccountIcon(account: Account): Int = when {
            account.isCard -> R.drawable.ic_account_card
            account.isElectronicMoney -> R.drawable.ic_account_electronic
            else -> when (account.accountType) {
                AccountType.CASH -> R.drawable.ic_account_cash
                AccountType.BANK -> R.drawable.ic_account_bank
                AccountType.ASSET -> R.drawable.ic_account_asset
                AccountType.LIABILITY -> R.drawable.ic_account_liability
                else -> R.drawable.ic_account_bank
            }
        }
    }

    object AccountDiffCallback : DiffUtil.ItemCallback<AccountListItem>() {
        override fun areItemsTheSame(oldItem: AccountListItem, newItem: AccountListItem): Boolean {
            return oldItem.account.id == newItem.account.id
        }

        override fun areContentsTheSame(oldItem: AccountListItem, newItem: AccountListItem): Boolean {
            return oldItem == newItem
        }
    }
}