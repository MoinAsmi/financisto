package ru.orangesoftware.financisto.repository.impl

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import ru.orangesoftware.financisto.repository.api.RepositoryException
import ru.orangesoftware.financisto.repository.api.RepositoryResult
import ru.orangesoftware.financisto.storage.api.dao.AccountDao

class AccountRepositoryErrorHandlingTest {
    private lateinit var repository: AccountRepositoryImpl
    private lateinit var accountDao: AccountDao

    @Before
    fun setup() {
        accountDao = mockk()
        repository = AccountRepositoryImpl(accountDao)
    }

    @Test
    fun `getAccounts should handle database errors`() = runTest {
        // Given
        val exception = RuntimeException("Database error")
        every { accountDao.getAccounts() } returns flow { throw exception }

        // When
        val results = repository.getAccounts().toList()

        // Then
        assertThat(results).hasSize(1)
        val result = results[0] as RepositoryResult.Error
        assertThat(result.exception).isInstanceOf(RepositoryException.DatabaseError::class.java)
        assertThat(result.exception.cause).isEqualTo(exception)
    }

    @Test
    fun `saveAccount should handle validation errors`() = runTest {
        // Given
        coEvery { accountDao.insertAccount(any()) } throws IllegalArgumentException("Invalid account")

        // When
        val result = repository.saveAccount(createDomainAccount(0))

        // Then
        assertThat(result).isInstanceOf(RepositoryResult.Error::class.java)
        result as RepositoryResult.Error
        assertThat(result.exception).isInstanceOf(RepositoryException.ValidationError::class.java)
        assertThat(result.exception.message).isEqualTo("Invalid account")
    }

    @Test
    fun `deleteAccount should handle non-existent account`() = runTest {
        // Given
        coEvery { accountDao.deleteAccount(any()) } throws IllegalArgumentException("Account not found")

        // When
        val result = repository.deleteAccount(999)

        // Then
        assertThat(result).isInstanceOf(RepositoryResult.Error::class.java)
        result as RepositoryResult.Error
        assertThat(result.exception).isInstanceOf(RepositoryException.ValidationError::class.java)
        assertThat(result.exception.message).isEqualTo("Account not found")
    }

    @Test
    fun `updateAccountBalance should handle invalid balance update`() = runTest {
        // Given
        coEvery { accountDao.updateAccountBalance(any(), any()) } throws RuntimeException("Balance update failed")

        // When
        val result = repository.updateAccountBalance(1, -1000)

        // Then
        assertThat(result).isInstanceOf(RepositoryResult.Error::class.java)
        result as RepositoryResult.Error
        assertThat(result.exception).isInstanceOf(RepositoryException.DatabaseError::class.java)
        assertThat(result.exception.message).isEqualTo("Balance update failed")
    }
}
