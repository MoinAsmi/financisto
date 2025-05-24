/*
 * Copyright © Microsoft Corporation. All rights reserved.
 */
package com.moin.financisto.data.entities

import androidx.room.TypeConverter
import java.lang.IllegalArgumentException

/**
 *
 */
class TypeConvertors {
    @TypeConverter
    fun fromAccountTypeToString(accountType: AccountType?) = accountType?.name

    @TypeConverter
    fun fromStringToAccountType(account: String?) = try {
        account?.let {
            AccountType.valueOf(it)
        }
    } catch (e: IllegalArgumentException) {
        null
    }
}
