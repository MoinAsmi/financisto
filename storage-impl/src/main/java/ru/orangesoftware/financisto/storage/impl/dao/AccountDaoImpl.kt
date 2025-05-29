package ru.orangesoftware.financisto.storage.impl.dao

import ru.orangesoftware.financisto.storage.api.dao.AccountDao
import ru.orangesoftware.financisto.storage.api.entities.Account
import kotlinx.coroutines.flow.*
import ru.orangesoftware.financisto.storage.impl.entities.AccountEntity

class AccountDaoImpl(
    private val accountRoomDao: AccountRoomDao
) : AccountDao {
    override fun getAccounts(): Flow<List<Account>> =
        accountRoomDao.getAccounts().map { it.map { accountEntity -> accountEntity.toDomain() } }

    override fun getAccountsByStatus(isActive: Boolean): Flow<List<Account>> =
        accountRoomDao.getAccountsByStatus(if (isActive) 1 else 0).map { it.map { accountEntity -> accountEntity.toDomain() } }

    override fun getAccountById(id: Long): Account? =
        accountRoomDao.getAccountById(id)?.toDomain()

    override suspend fun insert(account: Account): Long =
        accountRoomDao.insert(AccountEntity.fromDomain(account))

    override suspend fun update(account: Account) =
        accountRoomDao.update(AccountEntity.fromDomain(account))

    override suspend fun delete(accountId: Long) =
        accountRoomDao.delete(accountId)

    override suspend fun updateAccountBalance(accountId: Long, totalAmount: Long) =
        accountRoomDao.updateAccountBalance(accountId, totalAmount)
}
