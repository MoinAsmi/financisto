package ru.orangesoftware.financisto.feature.account.presentation

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.module.Module
import org.koin.dsl.module
import ru.orangesoftware.financisto.feature.account.R
import ru.orangesoftware.financisto.feature.account.test.FragmentTestRule
import ru.orangesoftware.financisto.feature.account.test.isDisplayedInView
import ru.orangesoftware.financisto.storage.api.entities.Account
import ru.orangesoftware.financisto.usecase.api.*

@RunWith(AndroidJUnit4::class)
class AccountListFragmentTest {

    private val getAccountList: GetAccountListUseCase = mockk()
    private val toggleAccountStatus: ToggleAccountStatusUseCase = mockk()
    private val deleteAccount: DeleteAccountUseCase = mockk()
    private val updateAccountBalance: UpdateAccountBalanceUseCase = mockk()

    private val testModule = module {
        single { getAccountList }
        single { toggleAccountStatus }
        single { deleteAccount }
        single { updateAccountBalance }
        single {
            AccountListViewModel(
                getAccountList = get(),
                toggleAccountStatus = get(),
                deleteAccount = get(),
                updateAccountBalance = get()
            )
        }
    }

    @get:Rule
    val fragmentRule = FragmentTestRule(
        fragmentClass = AccountListFragment::class.java,
        testModules = listOf(testModule)
    )

    @Test
    fun showsAccountListAndTotals() {
        // Given
        val accounts = listOf(
            createTestAccount(id = 1, title = "Cash", totalAmount = 1000L),
            createTestAccount(id = 2, title = "Bank", totalAmount = 2000L)
        )
        coEvery { getAccountList(any()) } returns flowOf(Result.success(accounts))

        // Then
        R.id.accountList.isDisplayedInView()
        onView(withText("Cash")).check(matches(isDisplayed()))
        onView(withText("Bank")).check(matches(isDisplayed()))
    }

    @Test
    fun showsErrorWhenLoadingFails() {
        // Given
        coEvery { getAccountList(any()) } returns flowOf(
            Result.failure(AccountUseCaseException.AccountNotFound(1L))
        )

        // Then
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(isDisplayed()))
    }

    @Test
    fun expandsAccountOnClick() {
        // Given
        val account = createTestAccount(id = 1, title = "Test Account")
        coEvery { getAccountList(any()) } returns flowOf(Result.success(listOf(account)))

        // When
        onView(withText("Test Account")).perform(click())

        // Then
        onView(withId(R.id.transactionsButton)).check(matches(isDisplayed()))
        onView(withId(R.id.editButton)).check(matches(isDisplayed()))
        onView(withId(R.id.deleteButton)).check(matches(isDisplayed()))
    }

    private fun createTestAccount(
        id: Long = 1L,
        title: String = "Test Account",
        totalAmount: Long = 1000L
    ) = Account(
        id = id,
        title = title,
        type = "CASH",
        currency = null,
        totalAmount = totalAmount,
        sortOrder = 0,
        isActive = true,
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
