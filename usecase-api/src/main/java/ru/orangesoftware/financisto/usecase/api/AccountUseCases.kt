package ru.orangesoftware.financisto.usecase.api

import kotlinx.coroutines.flow.Flow
import ru.orangesoftware.financisto.usecase.api.model.*
import java.time.LocalDate

interface GetAccountListUseCase {
    operator fun invoke(filter: AccountFilter): Flow<List<AccountWithBalance>>
}

interface GetAccountDetailsUseCase {
    suspend operator fun invoke(accountId: Long): Result<AccountDetails>
}

interface GetAccountTransactionsUseCase {
    operator fun invoke(accountId: Long, filter: TransactionFilter): Flow<List<Transaction>>
}

interface AddTransactionUseCase {
    suspend operator fun invoke(accountId: Long, transaction: Transaction): Result<Long>
}

interface AddTransferUseCase {
    suspend operator fun invoke(fromAccountId: Long, toAccountId: Long, transfer: Transfer): Result<Long>
}

interface UpdateAccountBalanceUseCase {
    suspend operator fun invoke(accountId: Long, newBalance: Money): Result<Unit>
}

interface PurgeAccountTransactionsUseCase {
    suspend operator fun invoke(accountId: Long, beforeDate: LocalDate): Result<Int>
}

interface ToggleAccountStatusUseCase {
    suspend operator fun invoke(accountId: Long): Result<Unit>
}

interface DeleteAccountUseCase {
    suspend operator fun invoke(accountId: Long): Result<Unit>
}

interface CalculateAccountTotalsUseCase {
    suspend operator fun invoke(): Result<AccountTotals>
}
