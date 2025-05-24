package com.moin.financisto.data.entities

import com.moin.myfinancisto.R

enum class AccountType(val title: String, val iconResId: Int) {
    Cash("Cash", R.drawable.account_type_cash),
    BankAccount("Bank Account", R.drawable.account_type_bank),
    Asset("Asset", R.drawable.account_type_asset),
    DebitCard("Debit Card", R.drawable.account_type_card),
    CreditCard("Credit Card", R.drawable.account_type_card),
    Wallet("Wallet", R.drawable.account_type_other)
}
