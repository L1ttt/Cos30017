package com.example.assignment2
import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import com.google.android.material.slider.Slider
import org.hamcrest.Matcher
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import android.app.Activity
import android.content.Intent
import androidx.activity.result.launch
import androidx.test.espresso.matcher.ViewMatchers
import java.util.concurrent.CompletableFuture

@RunWith(AndroidJUnit4::class)
class MainActivityEspressoTest {

    // This rule launches MainActivity before each test
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    private lateinit var decorView: View

    @Before
    fun setUp() {
        activityRule.scenario.onActivity { activity ->
            decorView = activity.window.decorView
        }
    }

    @Test
    fun nextButton_updatesInstrumentName() {
        onView(withId(R.id.itemName)).check(matches(withText("Guitar")))
        onView(withId(R.id.btnNext)).perform(ViewActions.click())
        onView(withId(R.id.itemName)).check(matches(withText("Keyboard")))
    }

    @Test
    fun previousButton_updatesInstrumentName() {
        // 1. Click "Next" to move to the second instrument first
        onView(withId(R.id.btnNext)).perform(ViewActions.click())
        onView(withId(R.id.itemName)).check(matches(withText("Keyboard")))
        onView(withId(R.id.btnPrevious)).perform(ViewActions.click())
        onView(withId(R.id.itemName)).check(matches(withText("Guitar")))
    }


}



@RunWith(AndroidJUnit4::class)
class DetailsActivityEspressoTest {

    private fun createTestInstrument(): Instrument {
        return Instrument(
            name = "Test Guitar",
            rating = 4.5f,
            attribute = "Acoustic",
            price = 500,
            imageRes = R.drawable.ic_launcher_background,
            borrowedMonths = 0,
            instrumentType = "Acoustic",
            instrumentModel = "Dreadnought",
            instrumentBrand = "TestBrand",
            instrumentColor = "Sunburst",
            instrumentCondition = "New"
        )
    }
    private fun setSliderValue(value: Float): ViewAction {
        return object : ViewAction {
            override fun getDescription(): String {
                return "Set a value on a Material Slider"
            }

            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(Slider::class.java)
            }

            override fun perform(uiController: UiController?, view: View?) {
                val slider = view as Slider
                slider.value = value
            }
        }
    }

    @Test
    fun detailsActivity_displaysCorrectInitialData() {
        // 1. Create the intent with test data
        val intent = Intent(ApplicationProvider.getApplicationContext(), DetailsActivity::class.java).apply {
            putExtra("selectedItem", createTestInstrument())
            putExtra("credit", 10000)
        }

        // 2. Launch the activity with the intent
        ActivityScenario.launch<DetailsActivity>(intent)

        // 3. Verify the data is displayed correctly
        onView(withId(R.id.detailItemName)).check(matches(withText("Test Guitar")))
        onView(withId(R.id.credit)).check(matches(withText("Credit: $10000")))
        // Price for 1 month (default slider value is 1.0f) should be $500
        onView(withId(R.id.detailItemPrice)).check(matches(withText("Price: $500")))
    }

    @Test
    fun sliderInteraction_updatesPrice() {
        // 1. Create the intent and launch the activity
        val intent = Intent(ApplicationProvider.getApplicationContext(), DetailsActivity::class.java).apply {
            putExtra("selectedItem", createTestInstrument())
            putExtra("credit", 10000)
        }
        ActivityScenario.launch<DetailsActivity>(intent)

        // 2. Move the slider to 3 months using our custom action
        onView(withId(R.id.borrowMonthsSlider)).perform(setSliderValue(3.0f))

        // 3. Verify the price updates to 3 * 500 = 1500
        onView(withId(R.id.detailItemPrice)).check(matches(withText("Price: $1500")))
    }


}



