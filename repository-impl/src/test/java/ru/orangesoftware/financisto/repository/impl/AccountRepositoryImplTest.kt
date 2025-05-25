package ru.orangesoftware.financisto.repository.impl

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import ru.orangesoftware.financisto.repository.api.RepositoryResult
import ru.orangesoftware.financisto.repository.api.model.Account
import ru.orangesoftware.financisto.repository.api.model.AccountType
import ru.orangesoftware.financisto.storage.api.dao.AccountDao
import ru.orangesoftware.financisto.storage.api.entities.Account as StorageAccount

class AccountRepositoryImplTest {
    private lateinit var repository: AccountRepositoryImpl
    private lateinit var accountDao: AccountDao

    @Before
    fun setup() {
        accountDao = mockk()
        repository = AccountRepositoryImpl(accountDao)
    }

    @Test
    fun `getAccounts should map storage accounts to domain accounts`() = runTest {
        // Given
        val storageAccounts = listOf(
            createStorageAccount(1),
            createStorageAccount(2)
        )
        every { accountDao.getAccounts() } returns flow { emit(storageAccounts) }

        // When
        val results = repository.getAccounts().toList()

        // Then
        assertThat(results).hasSize(1)
        val result = results[0] as RepositoryResult.Success
        assertThat(result.data).hasSize(2)
        assertThat(result.data[0].id).isEqualTo(1)
        assertThat(result.data[1].id).isEqualTo(2)
    }

    @Test
    fun `getAccountsByStatus should filter accounts by status`() = runTest {
        // Given
        val storageAccounts = listOf(
            createStorageAccount(1, isActive = true),
            createStorageAccount(2, isActive = false)
        )
        every { accountDao.getAccountsByStatus(true) } returns flow { emit(storageAccounts.filter { it.isActive }) }

        // When
        val results = repository.getAccountsByStatus(true).toList()

        // Then
        assertThat(results).hasSize(1)
        val result = results[0] as RepositoryResult.Success
        assertThat(result.data).hasSize(1)
        assertThat(result.data[0].id).isEqualTo(1)
        assertThat(result.data[0].isActive).isTrue()
    }

    @Test
    fun `saveAccount should insert new account when id is 0`() = runTest {
        // Given
        val account = createDomainAccount(0)
        coEvery { accountDao.insertAccount(any()) } returns 1L

        // When
        val result = repository.saveAccount(account)

        // Then
        assertThat(result).isInstanceOf(RepositoryResult.Success::class.java)
        result as RepositoryResult.Success
        assertThat(result.data).isEqualTo(1L)
        coVerify { accountDao.insertAccount(any()) }
    }

    @Test
    fun `saveAccount should update existing account when id is not 0`() = runTest {
        // Given
        val account = createDomainAccount(1)
        coEvery { accountDao.updateAccount(any()) } returns Unit

        // When
        val result = repository.saveAccount(account)

        // Then
        assertThat(result).isInstanceOf(RepositoryResult.Success::class.java)
        result as RepositoryResult.Success
        assertThat(result.data).isEqualTo(1L)
        coVerify { accountDao.updateAccount(any()) }
    }

    @Test
    fun `deleteAccount should return success when deletion is successful`() = runTest {
        // Given
        coEvery { accountDao.deleteAccount(1) } returns Unit

        // When
        val result = repository.deleteAccount(1)

        // Then
        assertThat(result).isInstanceOf(RepositoryResult.Success::class.java)
        coVerify { accountDao.deleteAccount(1) }
    }

    @Test
    fun `updateAccountBalance should update balance successfully`() = runTest {
        // Given
        coEvery { accountDao.updateAccountBalance(1, 100) } returns Unit

        // When
        val result = repository.updateAccountBalance(1, 100)

        // Then
        assertThat(result).isInstanceOf(RepositoryResult.Success::class.java)
        coVerify { accountDao.updateAccountBalance(1, 100) }
    }

    private fun createStorageAccount(
        id: Long,
        isActive: Boolean = true
    ) = StorageAccount(
        id = id,
        title = "Account $id",
        type = StorageAccount.Type.CASH,
        currencyId = 1,
        totalAmount = 0,
        isActive = isActive
    )

    private fun createDomainAccount(
        id: Long,
        isActive: Boolean = true
    ) = Account(
        id = id,
        title = "Account $id",
        type = AccountType.CASH,
        currencyId = 1,
        totalAmount = 0,
        isActive = isActive
    )
}
