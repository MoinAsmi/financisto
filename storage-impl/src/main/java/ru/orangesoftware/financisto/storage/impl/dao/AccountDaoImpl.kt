package ru.orangesoftware.financisto.storage.impl.dao

import ru.orangesoftware.financisto.storage.api.dao.AccountDao
import ru.orangesoftware.financisto.storage.api.entities.Account
import kotlinx.coroutines.flow.*
import ru.orangesoftware.financisto.storage.impl.entities.AccountEntity

class AccountDaoImpl(
    private val accountRoomDao: AccountRoomDao
) : AccountDao {
    override fun getAccounts(): Flow<List<Account>> =
        accountRoomDao.getAccountsWithCurrency()
            .map { accountsWithCurrency ->
                accountsWithCurrency.map { (account, currency) ->
                    account.toDomain(currency.toDomain())
                }
            }

    override fun getActiveAccounts(): Flow<List<Account>> =
        accountRoomDao.getActiveAccountsWithCurrency()
            .map { accountsWithCurrency ->
                accountsWithCurrency.map { (account, currency) ->
                    account.toDomain(currency.toDomain())
                }
            }

    override fun getAccountById(id: Long): Flow<Account?> =
        accountRoomDao.getAccountWithCurrencyById(id)
            .map { accountWithCurrency ->
                accountWithCurrency?.let { (account, currency) ->
                    account.toDomain(currency.toDomain())
                }
            }

    override suspend fun insert(account: Account): Long =
        accountRoomDao.insert(AccountEntity.fromDomain(account))

    override suspend fun update(account: Account) =
        accountRoomDao.update(AccountEntity.fromDomain(account))

    override suspend fun delete(accountId: Long) =
        accountRoomDao.delete(accountId)

    override suspend fun updateAccountBalance(accountId: Long, totalAmount: Long) =
        accountRoomDao.updateAccountBalance(accountId, totalAmount)
}
