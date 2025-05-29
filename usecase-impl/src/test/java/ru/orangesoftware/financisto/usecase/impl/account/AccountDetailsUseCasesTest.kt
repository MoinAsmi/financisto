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
import ru.orangesoftware.financisto.repository.api.model.Transaction as RepoTransaction
import ru.orangesoftware.financisto.usecase.api.model.TransactionFilter
import java.time.LocalDateTime

class GetAccountDetailsUseCaseImplTest {
    private lateinit var repository: AccountRepository
    private lateinit var useCase: GetAccountDetailsUseCaseImpl

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetAccountDetailsUseCaseImpl(repository)
    }

    @Test
    fun `getAccountDetails returns mapped details when repository succeeds`() = runTest {
        // Given
        val account = createRepoAccount(1)
        coEvery { repository.getAccount(1) } returns RepositoryResult.Success(account)

        // When
        val result = useCase(1)

        // Then
        assertThat(result.isSuccess).isTrue()
        result.onSuccess { details ->
            assertThat(details.account.id).isEqualTo(1)
            assertThat(details.balance.amount.toLong()).isEqualTo(1000L)
        }
    }

    @Test
    fun `getAccountDetails returns failure when repository fails`() = runTest {
        // Given
        coEvery { repository.getAccount(1) } returns RepositoryResult.Error(
            ru.orangesoftware.financisto.repository.api.RepositoryException.NotFound("Account not found")
        )

        // When
        val result = useCase(1)

        // Then
        assertThat(result.isFailure).isTrue()
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

class GetAccountTransactionsUseCaseImplTest {
    private lateinit var repository: AccountRepository
    private lateinit var useCase: GetAccountTransactionsUseCaseImpl

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetAccountTransactionsUseCaseImpl(repository)
    }

    @Test
    fun `getTransactions returns mapped transactions when repository succeeds`() = runTest {
        // Given
        val transactions = listOf(
            createRepoTransaction(1),
            createRepoTransaction(2)
        )
        every { repository.getAccountTransactions(1) } returns flow {
            emit(RepositoryResult.Success(transactions))
        }

        // When
        val result = useCase(1, TransactionFilter()).single()

        // Then
        assertThat(result).hasSize(2)
        assertThat(result[0].id).isEqualTo(1)
        assertThat(result[1].id).isEqualTo(2)
    }

    @Test(expected = Exception::class)
    fun `getTransactions propagates error when repository fails`() = runTest {
        // Given
        every { repository.getAccountTransactions(1) } returns flow {
            emit(RepositoryResult.Error(Exception("Database error")))
        }

        // When
        useCase(1, TransactionFilter()).single()

        // Then - exception is thrown
    }

    private fun createRepoTransaction(id: Long) = RepoTransaction(
        id = id,
        accountId = 1,
        amount = 100L,
        type = RepoTransaction.Type.EXPENSE,
        date = System.currentTimeMillis()
    )
}
