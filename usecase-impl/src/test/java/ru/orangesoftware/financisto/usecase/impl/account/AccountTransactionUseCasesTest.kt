package ru.orangesoftware.financisto.usecase.impl.account

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import ru.orangesoftware.financisto.repository.api.AccountRepository
import ru.orangesoftware.financisto.repository.api.RepositoryResult
import ru.orangesoftware.financisto.repository.api.model.Account as RepoAccount
import ru.orangesoftware.financisto.repository.api.model.Transaction as RepoTransaction
import ru.orangesoftware.financisto.repository.api.model.Transfer as RepoTransfer
import ru.orangesoftware.financisto.usecase.api.AccountUseCaseException
import ru.orangesoftware.financisto.usecase.api.model.Money
import ru.orangesoftware.financisto.usecase.api.model.Transaction
import ru.orangesoftware.financisto.usecase.api.model.Transfer
import ru.orangesoftware.financisto.usecase.api.model.TransactionType
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

class AddTransactionUseCaseImplTest {
    private lateinit var repository: AccountRepository
    private lateinit var useCase: AddTransactionUseCaseImpl

    @Before
    fun setup() {
        repository = mockk()
        useCase = AddTransactionUseCaseImpl(repository)
    }

    @Test
    fun `addTransaction returns success when repository succeeds`() = runTest {
        // Given
        val transaction = createTransaction()
        coEvery { repository.saveTransaction(any()) } returns RepositoryResult.Success(1L)

        // When
        val result = useCase(1, transaction)

        // Then
        assertThat(result.isSuccess).isTrue()
        result.onSuccess { id ->
            assertThat(id).isEqualTo(1L)
        }
    }

    private fun createTransaction() = Transaction(
        accountId = 1,
        type = TransactionType.EXPENSE,
        amount = mockk<Money>(),
        date = LocalDateTime.now()
    )
}

class AddTransferUseCaseImplTest {
    private lateinit var repository: AccountRepository
    private lateinit var useCase: AddTransferUseCaseImpl

    @Before
    fun setup() {
        repository = mockk()
        useCase = AddTransferUseCaseImpl(repository)
    }

    @Test
    fun `addTransfer fails when same account`() = runTest {
        // Given
        val transfer = createTransfer()

        // When
        val result = useCase(1, 1, transfer)

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(AccountUseCaseException.SameAccountTransfer::class.java)
    }

    @Test
    fun `addTransfer checks currency compatibility`() = runTest {
        // Given
        val transfer = createTransfer()
        val account1 = createRepoAccount(1, "USD")
        val account2 = createRepoAccount(2, "EUR")
        coEvery { repository.getAccount(1) } returns RepositoryResult.Success(account1)
        coEvery { repository.getAccount(2) } returns RepositoryResult.Success(account2)

        // When
        val result = useCase(1, 2, transfer)

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(AccountUseCaseException.IncompatibleCurrencies::class.java)
    }

    private fun createTransfer() = Transfer(
        amount = mockk(),
        fromAccountAmount = mockk(),
        toAccountAmount = mockk(),
        date = LocalDateTime.now()
    )

    private fun createRepoAccount(id: Long, currencyCode: String) = RepoAccount(
        id = id,
        title = "Account $id",
        type = RepoAccount.Type.CASH,
        isActive = true,
        sortOrder = 0,
        totalAmount = 1000L,
        limitAmount = 0L,
        currency = mockk {
            every { name } returns currencyCode
        }
    )
}

class UpdateAccountBalanceUseCaseImplTest {
    private lateinit var repository: AccountRepository
    private lateinit var useCase: UpdateAccountBalanceUseCaseImpl

    @Before
    fun setup() {
        repository = mockk()
        useCase = UpdateAccountBalanceUseCaseImpl(repository)
    }

    @Test
    fun `updateBalance validates currency match`() = runTest {
        // Given
        val account = createRepoAccount(1, "USD")
        val newBalance = mockk<Money> {
            every { currency } returns mockk {
                every { name } returns "EUR"
            }
        }
        coEvery { repository.getAccount(1) } returns RepositoryResult.Success(account)

        // When
        val result = useCase(1, newBalance)

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(AccountUseCaseException.InvalidBalanceUpdate::class.java)
    }

    private fun createRepoAccount(id: Long, currencyCode: String) = RepoAccount(
        id = id,
        title = "Account $id",
        type = RepoAccount.Type.CASH,
        isActive = true,
        sortOrder = 0,
        totalAmount = 1000L,
        limitAmount = 0L,
        currency = mockk {
            every { name } returns currencyCode
        }
    )
}

class PurgeAccountTransactionsUseCaseImplTest {
    private lateinit var repository: AccountRepository
    private lateinit var useCase: PurgeAccountTransactionsUseCaseImpl

    @Before
    fun setup() {
        repository = mockk()
        useCase = PurgeAccountTransactionsUseCaseImpl(repository)
    }

    @Test
    fun `purgeTransactions fails for future date`() = runTest {
        // Given
        val futureDate = LocalDate.now().plusDays(1)

        // When
        val result = useCase(1, futureDate)

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(AccountUseCaseException.InvalidPurgeDate::class.java)
    }

    @Test
    fun `purgeTransactions verifies account exists`() = runTest {
        // Given
        coEvery { repository.getAccount(1) } returns RepositoryResult.Error(
            ru.orangesoftware.financisto.repository.api.RepositoryException.NotFound("Account not found")
        )

        // When
        val result = useCase(1, LocalDate.now())

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(AccountUseCaseException.AccountNotFound::class.java)
    }
}
