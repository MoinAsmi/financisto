package ru.orangesoftware.financisto.repository.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.orangesoftware.financisto.repository.api.AccountRepository
import ru.orangesoftware.financisto.repository.api.RepositoryResult
import ru.orangesoftware.financisto.repository.api.model.Account
import ru.orangesoftware.financisto.repository.impl.mapper.toDomain
import ru.orangesoftware.financisto.repository.impl.mapper.toStorage
import ru.orangesoftware.financisto.storage.api.dao.AccountDao

class AccountRepositoryImpl(
    private val accountDao: AccountDao
) : BaseRepository(), AccountRepository {

    override fun getAccounts(): Flow<RepositoryResult<List<Account>>> = executeFlow {
        accountDao.getAccounts().map { accounts -> 
            accounts.map { it.toDomain() }
        }
    }

    override fun getAccountsByStatus(isActive: Boolean): Flow<RepositoryResult<List<Account>>> = executeFlow {
        accountDao.getAccountsByStatus(isActive).map { accounts ->
            accounts.map { it.toDomain() }
        }
    }

    override suspend fun getAccount(id: Long): RepositoryResult<Account> = execute {
        accountDao.getAccountById(id)?.toDomain()
    }

    override suspend fun saveAccount(account: Account): RepositoryResult<Long> = execute {
        val storageAccount = account.toStorage()
        if (storageAccount.id == 0L) {
            accountDao.insert(storageAccount)
        } else {
            accountDao.update(storageAccount)
            storageAccount.id
        }
    }

    override suspend fun deleteAccount(id: Long): RepositoryResult<Unit> = execute {
        accountDao.delete(id)
    }

    override suspend fun updateAccountBalance(id: Long, deltaAmount: Long): RepositoryResult<Unit> = execute {
        accountDao.updateAccountBalance(id, deltaAmount)
    }
}
