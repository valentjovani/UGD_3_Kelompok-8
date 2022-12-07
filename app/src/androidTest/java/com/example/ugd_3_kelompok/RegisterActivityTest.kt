package com.example.ugd_3_kelompok


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class RegisterActivityTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(RegisterActivity::class.java)

    @Test
    fun registerActivityTest() {
        val textInputEditText = onView(
            allOf(
                withId(R.id.inputUsername),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.etUsername),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText.perform(replaceText("test"), closeSoftKeyboard())


        val textInputEditText2 = onView(
            allOf(
                childAtPosition(
                    childAtPosition(
                        withId(R.id.etPassword),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText2.perform(replaceText("123"), closeSoftKeyboard())

        val textInputEditText3 = onView(
            allOf(
                withId(R.id.inputEmail),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.etEmail),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText3.perform(replaceText("example@gmail.com"), closeSoftKeyboard())

        val materialButton = onView(
            allOf(
                withId(R.id.btnDatePicker), withText("Date Pick"),
                childAtPosition(
                    allOf(
                        withId(R.id.LinearLayout),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            2
                        )
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())

        val materialButton2 = onView(
            allOf(
                withId(android.R.id.button1), withText("OK"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    3
                )
            )
        )
        materialButton2.perform(scrollTo(), click())

        val textInputEditText4 = onView(
            allOf(
                childAtPosition(
                    childAtPosition(
                        withId(R.id.etNomorTelepon),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText4.perform(replaceText("123456789"), closeSoftKeyboard())

        val materialButton3 = onView(
            allOf(
                withId(R.id.btnRegister), withText("REGISTER"),
                childAtPosition(
                    allOf(
                        withId(R.id.linearLayout2),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            3
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialButton3.perform(click())
        onView(isRoot()).perform(waitFor(3000))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }

    fun waitFor(delay: Long): ViewAction? {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isRoot()
            }

            override fun getDescription(): String {
                return "wait for" + delay + "milliseconds"
            }

            override fun perform(uiController: UiController, view: View?) {
                uiController.loopMainThreadForAtLeast(delay)
            }
        }
    }
}
