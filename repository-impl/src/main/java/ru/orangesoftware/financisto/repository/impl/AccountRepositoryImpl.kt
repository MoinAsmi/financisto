package ru.orangesoftware.financisto.repository.impl

import kotlinx.coroutines.flow.Flow
import ru.orangesoftware.financisto.repository.api.AccountRepository
import ru.orangesoftware.financisto.repository.api.RepositoryResult
import ru.orangesoftware.financisto.storage.api.dao.AccountDao
import ru.orangesoftware.financisto.storage.api.entities.Account

class AccountRepositoryImpl(
    private val accountDao: AccountDao
) : BaseRepository(), AccountRepository {

    override fun getAccounts(): Flow<RepositoryResult<List<Account>>> = executeFlow {
        accountDao.getAccounts()
    }

    override fun getAccountsByStatus(isActive: Boolean): Flow<RepositoryResult<List<Account>>> = executeFlow {
        accountDao.getAccountsByStatus(isActive)
    }

    override suspend fun getAccount(id: Long): RepositoryResult<Account> = execute {
        accountDao.getAccount(id)
    }

    override suspend fun saveAccount(account: Account): RepositoryResult<Long> = execute {
        if (account.id == 0L) {
            accountDao.insertAccount(account)
        } else {
            accountDao.updateAccount(account)
            account.id
        }
    }

    override suspend fun deleteAccount(account: Account): RepositoryResult<Unit> = execute {
        accountDao.deleteAccount(account)
    }

    override suspend fun updateAccountBalance(id: Long, deltaAmount: Long): RepositoryResult<Unit> = execute {
        accountDao.updateAccountBalance(id, deltaAmount)
    }
}
