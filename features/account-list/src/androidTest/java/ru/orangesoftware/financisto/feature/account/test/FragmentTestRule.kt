package ru.orangesoftware.financisto.feature.account.test

import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module

class FragmentTestRule<F : Fragment>(
    private val fragmentClass: Class<F>,
    private val testModules: List<Module> = emptyList()
) : TestRule {

    private var scenario: FragmentScenario<F>? = null

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                setup()
                try {
                    base.evaluate()
                } finally {
                    cleanup()
                }
            }
        }
    }

    private fun setup() {
        if (testModules.isNotEmpty()) {
            loadKoinModules(testModules)
        }
        scenario = launchFragmentInContainer(
            themeResId = androidx.appcompat.R.style.Theme_AppCompat
        )
        scenario?.moveToState(Lifecycle.State.RESUMED)
    }

    private fun cleanup() {
        if (testModules.isNotEmpty()) {
            unloadKoinModules(testModules)
        }
        scenario?.moveToState(Lifecycle.State.DESTROYED)
        scenario = null
    }

    fun runOnFragment(action: (F) -> Unit) {
        scenario?.onFragment { fragment ->
            action(fragment)
        }
    }
}

// Extension functions for Espresso testing
fun Int.isDisplayedInView(): ViewInteraction =
    Espresso.onView(withId(this)).check(matches(isDisplayed()))
