package com.moin.financisto.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.ForeignKey.Companion.RESTRICT
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "expenses",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = ExpenseType::class,
            parentColumns = ["expenseTypeId"],
            childColumns = ["expenseTypeId"],
            onDelete = RESTRICT
        ),
        ForeignKey(
            entity = Account::class,
            parentColumns = ["accountId"],
            childColumns = ["accountId"],
            onDelete = RESTRICT
        )
    ],
    indices = [
        Index(value = ["userId"]),
        Index(value = ["expenseTypeId"]),
        Index(value = ["accountId"]),
        Index(value = ["expenseId", "date"], unique = true)
    ]
)
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val expenseId: Long = 0,
    val userId: Long,
    val expenseTypeId: Long,
    val accountId: Long,
    val amount: Double,
    val date: Long,
    val notes: String?
)

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Long = 0,
    val name: String,
    val email: String
)

@Entity(
    tableName = "expense_types",
    indices = [
        Index(value = ["type"], unique = true),
        Index(value = ["parentExpenseTypeId"], unique = false)
    ],
    foreignKeys = [
        ForeignKey(
            entity = ExpenseType::class,
            parentColumns = ["expenseTypeId"],
            childColumns = ["parentExpenseTypeId"],
            onDelete = CASCADE
        )
    ]
)
data class ExpenseType(
    @PrimaryKey(autoGenerate = true)
    val expenseTypeId: Long = 0,
    val type: String,
    val parentExpenseTypeId: Long?
)

@Entity(
    tableName = "accounts",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = CASCADE
        )
    ],
    indices = [
        Index(value = ["userId"])
    ]
)
data class Account(
    @PrimaryKey(autoGenerate = true)
    val accountId: Long = 0,
    val userId: Long,
    val name: String
)

@Entity(
    tableName = "expense_attributes",
    foreignKeys = [
        ForeignKey(
            entity = ExpenseType::class,
            parentColumns = ["expenseTypeId"],
            childColumns = ["expenseTypeId"],
            onDelete = CASCADE
        )
    ],
    indices = [
        Index(value = ["expenseTypeId"])
    ]
)
data class ExpenseAttribute(
    @PrimaryKey(autoGenerate = true)
    val attributeId: Long = 0,
    val expenseTypeId: Long,
    val name: String,
    val type: String
)


@Entity(
    tableName = "expense_attribute_values",
    foreignKeys = [
        ForeignKey(
            entity = Expense::class,
            parentColumns = ["expenseId"],
            childColumns = ["expenseId"],
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = ExpenseAttribute::class,
            parentColumns = ["attributeId"],
            childColumns = ["attributeId"],
            onDelete = CASCADE
        )
    ],
    primaryKeys = [ "attributeId", "expenseId" ],
    indices = [
        Index(value = ["expenseId"])
    ]
)
data class ExpenseAttributeValue(
    val attributeId: Long,
    val expenseId: Long,
    val value: String
)

@Entity(tableName = "running_balances",
    foreignKeys = [
        ForeignKey(entity = Expense::class,
            parentColumns = ["expenseId", "date"],
            childColumns = ["transactionId", "date"],
            onDelete = CASCADE),
        ForeignKey(entity = Account::class,
            parentColumns = ["accountId"],
            childColumns = ["accountId"],
            onDelete = CASCADE)
    ])
data class RunningBalance(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val accountId: Long,
    val transactionId: Long,
    val date: Long,
    val balance: Double
)
