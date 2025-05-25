package ru.orangesoftware.financisto.repository.api

import kotlinx.coroutines.flow.Flow
import ru.orangesoftware.financisto.repository.api.model.Account

interface AccountRepository {
    /**
     * Get all accounts as a Flow.
     * The Flow will emit new values whenever the accounts data changes.
     */
    fun getAccounts(): Flow<RepositoryResult<List<Account>>>

    /**
     * Get a single account by its ID.
     */
    suspend fun getAccount(id: Long): RepositoryResult<Account>

    /**
     * Create a new account or update an existing one.
     * If the account's id is 0, it will be created; otherwise, it will be updated.
     * Returns the account ID on success.
     */
    suspend fun saveAccount(account: Account): RepositoryResult<Long>

    /**
     * Delete an account by its ID.
     * This will fail if there are any transactions associated with the account.
     */
    suspend fun deleteAccount(id: Long): RepositoryResult<Unit>

    /**
     * Get accounts filtered by their active status
     */
    fun getAccountsByStatus(isActive: Boolean): Flow<RepositoryResult<List<Account>>>

    /**
     * Update account balance
     * This is used when transactions affect the account balance
     */
    suspend fun updateAccountBalance(id: Long, deltaAmount: Long): RepositoryResult<Unit>
}
