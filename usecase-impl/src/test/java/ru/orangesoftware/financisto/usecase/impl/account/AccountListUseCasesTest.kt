package ru.orangesoftware.financisto.usecase.impl.account

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import ru.orangesoftware.financisto.repository.api.AccountRepository
import ru.orangesoftware.financisto.repository.api.RepositoryResult
import ru.orangesoftware.financisto.repository.api.model.Account as RepoAccount
import ru.orangesoftware.financisto.usecase.api.model.AccountFilter
import ru.orangesoftware.financisto.usecase.api.model.AccountWithBalance
import java.math.BigDecimal

class GetAccountListUseCaseImplTest {
    private lateinit var repository: AccountRepository
    private lateinit var useCase: GetAccountListUseCaseImpl

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetAccountListUseCaseImpl(repository)
    }

    @Test
    fun `getAccounts returns mapped accounts when repository succeeds`() = runTest {
        // Given
        val repoAccounts = listOf(
            createRepoAccount(1),
            createRepoAccount(2)
        )
        every { repository.getAccountsByStatus(any()) } returns flow {
            emit(RepositoryResult.Success(repoAccounts))
        }

        // When
        val result = useCase(AccountFilter(showInactive = false)).single()

        // Then
        assertThat(result).hasSize(2)
        assertThat(result[0].account.id).isEqualTo(1)
        assertThat(result[1].account.id).isEqualTo(2)
    }

    @Test(expected = Exception::class)
    fun `getAccounts propagates error when repository fails`() = runTest {
        // Given
        every { repository.getAccountsByStatus(any()) } returns flow {
            emit(RepositoryResult.Error(Exception("Database error")))
        }

        // When
        useCase(AccountFilter(showInactive = false)).single()

        // Then - exception is thrown
    }

    private fun createRepoAccount(id: Long) = RepoAccount(
        id = id,
        title = "Account $id",
        type = RepoAccount.Type.CASH,
        isActive = true,
        sortOrder = 0,
        totalAmount = 1000L,
        limitAmount = 0L,
        currency = mockk {
            every { name } returns "USD"
            every { symbol } returns "$"
            every { decimalPlaces } returns 2
        }
    )
}

class ToggleAccountStatusUseCaseImplTest {
    private lateinit var repository: AccountRepository
    private lateinit var useCase: ToggleAccountStatusUseCaseImpl

    @Before
    fun setup() {
        repository = mockk()
        useCase = ToggleAccountStatusUseCaseImpl(repository)
    }

    @Test
    fun `toggleStatus flips account status when successful`() = runTest {
        // Given
        val account = createRepoAccount(1, isActive = true)
        coEvery { repository.getAccount(1) } returns RepositoryResult.Success(account)
        coEvery { repository.saveAccount(any()) } returns RepositoryResult.Success(1L)

        // When
        val result = useCase(1)

        // Then
        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `toggleStatus returns failure when account not found`() = runTest {
        // Given
        coEvery { repository.getAccount(1) } returns RepositoryResult.Error(
            ru.orangesoftware.financisto.repository.api.RepositoryException.NotFound("Account not found")
        )

        // When
        val result = useCase(1)

        // Then
        assertThat(result.isFailure).isTrue()
    }

    private fun createRepoAccount(id: Long, isActive: Boolean = true) = RepoAccount(
        id = id,
        title = "Account $id",
        type = RepoAccount.Type.CASH,
        isActive = isActive,
        sortOrder = 0,
        totalAmount = 1000L,
        limitAmount = 0L,
        currency = mockk()
    )
}

class DeleteAccountUseCaseImplTest {
    private lateinit var repository: AccountRepository
    private lateinit var useCase: DeleteAccountUseCaseImpl

    @Before
    fun setup() {
        repository = mockk()
        useCase = DeleteAccountUseCaseImpl(repository)
    }

    @Test
    fun `deleteAccount returns success when repository succeeds`() = runTest {
        // Given
        coEvery { repository.deleteAccount(1) } returns RepositoryResult.Success(Unit)

        // When
        val result = useCase(1)

        // Then
        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `deleteAccount returns failure when repository fails`() = runTest {
        // Given
        coEvery { repository.deleteAccount(1) } returns RepositoryResult.Error(Exception("Delete failed"))

        // When
        val result = useCase(1)

        // Then
        assertThat(result.isFailure).isTrue()
    }
}
