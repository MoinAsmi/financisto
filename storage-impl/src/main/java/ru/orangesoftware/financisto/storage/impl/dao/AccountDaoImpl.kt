package ru.orangesoftware.financisto.storage.impl.dao

import ru.orangesoftware.financisto.storage.api.dao.AccountDao
import ru.orangesoftware.financisto.storage.api.entities.Account
import kotlinx.coroutines.flow.*
import ru.orangesoftware.financisto.storage.impl.entities.AccountEntity

class AccountDaoImpl(
    private val accountRoomDao: AccountRoomDao
) : AccountDao {
    override fun getAccounts(): Flow<List<Account>> =
        accountRoomDao.getAccounts().map { accounts -> accounts.map { it.toDomain() } }

    override fun getAccountsByStatus(isActive: Boolean): Flow<List<Account>> =
        accountRoomDao.getAccountsByStatus(isActive).map { accounts -> accounts.map { it.toDomain() } }

    override suspend fun getAccount(id: Long): Account? =
        accountRoomDao.getAccount(id)?.toDomain()

    override suspend fun insertAccount(account: Account): Long =
        accountRoomDao.insertAccount(AccountEntity.fromDomain(account))

    override suspend fun updateAccount(account: Account) =
        accountRoomDao.updateAccount(AccountEntity.fromDomain(account))

    override suspend fun deleteAccount(account: Account) =
        accountRoomDao.deleteAccount(AccountEntity.fromDomain(account))

    override suspend fun updateAccountBalance(accountId: Long, totalAmount: Long) =
        accountRoomDao.updateAccountBalance(accountId, totalAmount)
}
