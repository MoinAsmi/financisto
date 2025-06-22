package ru.orangesoftware.financisto.feature.account.presentation

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import ru.orangesoftware.financisto.storage.api.entities.Account
import ru.orangesoftware.financisto.storage.api.entities.Currency
import ru.orangesoftware.financisto.usecase.api.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AccountListViewModelTest {

    private val getAccountList: GetAccountListUseCase = mockk()
    private val toggleAccountStatus: ToggleAccountStatusUseCase = mockk()
    private val deleteAccount: DeleteAccountUseCase = mockk()
    private val updateAccountBalance: UpdateAccountBalanceUseCase = mockk()
    
    private lateinit var viewModel: AccountListViewModel
    private lateinit var testDispatcher: TestDispatcher

    @Before
    fun setup() {
        testDispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(testDispatcher)
        
        viewModel = AccountListViewModel(
            getAccountList = getAccountList,
            toggleAccountStatus = toggleAccountStatus,
            deleteAccount = deleteAccount,
            updateAccountBalance = updateAccountBalance
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when initialized, loads active accounts`() = runTest {
        // Given
        val accounts = listOf(createTestAccount(id = 1))
        coEvery { getAccountList(true) } returns flowOf(Result.success(accounts))

        // When initialized in setup()

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertTrue(state.filterActive)
            assertEquals(accounts.size, state.accounts.size)
            assertEquals(accounts[0].id, state.accounts[0].account.id)
        }
    }

    @Test
    fun `when filter toggled, loads all accounts`() = runTest {
        // Given
        val activeAccounts = listOf(createTestAccount(id = 1, isActive = true))
        val allAccounts = listOf(
            createTestAccount(id = 1, isActive = true),
            createTestAccount(id = 2, isActive = false)
        )
        coEvery { getAccountList(true) } returns flowOf(Result.success(activeAccounts))
        coEvery { getAccountList(false) } returns flowOf(Result.success(allAccounts))

        // When
        viewModel.onEvent(AccountListEvent.FilterToggled)

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertFalse(state.filterActive)
            assertEquals(allAccounts.size, state.accounts.size)
        }
    }

    @Test
    fun `when account deleted, shows success and reloads`() = runTest {
        // Given
        val accountId = 1L
        coEvery { deleteAccount(accountId) } returns Result.success(Unit)
        coEvery { getAccountList(any()) } returns flowOf(Result.success(emptyList()))

        // When
        viewModel.onEvent(AccountListEvent.AccountDeleted(accountId))

        // Then
        coVerify { deleteAccount(accountId) }
        coVerify { getAccountList(any()) }
    }

    @Test
    fun `when error occurs, shows error state`() = runTest {
        // Given
        val error = AccountUseCaseException.AccountNotFound(1L)
        coEvery { getAccountList(any()) } returns flowOf(Result.failure(error))

        // When initialized in setup()

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertTrue(state.error is AccountListError.LoadError)
        }
    }

    private fun createTestAccount(
        id: Long = 1L,
        title: String = "Test Account",
        type: String = "CASH",
        isActive: Boolean = true,
        totalAmount: Long = 1000L
    ) = Account(
        id = id,
        title = title,
        type = type,
        currency = Currency(
            id = 1L,
            title = "USD",
            name = "USD",
            symbol = "$",
            isDefault = true,
            decimals = 2,
            decimalSeparator = ".",
            groupSeparator = ",",
            symbol1Position = 0,
            symbol2Position = 0
        ),
        totalAmount = totalAmount,
        sortOrder = 0,
        isActive = isActive,
        isIncludeIntoTotals = true,
        lastTransactionDate = null,
        lastReconciliationDate = null,
        closingDay = 0,
        paymentDay = 0,
        note = null,
        limitAmount = 0L,
        cardIssuer = null,
        issuer = null,
        number = null,
        creationDate = System.currentTimeMillis()
    )
}
