package com.openclassrooms.realestatemanager.ui.activity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.util.TestUtils.withRecyclerView
import com.openclassrooms.realestatemanager.util.Utils.fromDollarToEuro
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

// IMPORTANT : For tests to work correctly, you need to disable animations and
// the 'Pop on screen' option of the app from the phone's settings (in "Apps and notifications")

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {


    @Before
    fun setUp() = Intents.init()

    @After
    fun teardown() = Intents.release()


    @Rule
    @JvmField
    var rule = ActivityTestRule(MainActivity::class.java)

    @Rule
    @JvmField
    var mGrantPermissionRule =
        GrantPermissionRule.grant(
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"
        )

    @Test
    fun test_navigation_to_add_edit_estate_activity() = runTest {
        onView(withId(R.id.main_list_bt_add))
            .perform(click())

        Intents.intended(hasComponent(AddEditEstateActivity::class.java.name))
    }

    @Test
    fun test_add_an_estate() {
        test_navigation_to_add_edit_estate_activity()

        onView(withId(R.id.add_edit_estate_title))
            .perform(
                scrollTo(),
                ViewActions.replaceText("Apartment in Paris"),
                ViewActions.closeSoftKeyboard()
            )

        onView(withId(R.id.add_edit_estate_country))
            .perform(
                scrollTo(),
                ViewActions.replaceText("France"),
                ViewActions.closeSoftKeyboard()
            )

        onView(withId(R.id.add_edit_estate_city))
            .perform(
                scrollTo(),
                ViewActions.replaceText("Paris"),
                ViewActions.closeSoftKeyboard()
            )

        onView(withId(R.id.add_edit_estate_zip))
            .perform(
                scrollTo(),
                ViewActions.replaceText("75011"),
                ViewActions.closeSoftKeyboard()
            )

        onView(withId(R.id.add_edit_estate_address))
            .perform(
                scrollTo(),
                ViewActions.replaceText("96 Avenue de la République"),
                ViewActions.closeSoftKeyboard()
            )

        onView(withId(R.id.add_edit_estate_area))
            .perform(
                scrollTo(),
                ViewActions.replaceText("50"),
                ViewActions.closeSoftKeyboard()
            )

        onView(withId(R.id.add_edit_estate_price))
            .perform(
                scrollTo(),
                ViewActions.replaceText("750000"),
                ViewActions.closeSoftKeyboard()
            )

        onView(withId(R.id.add_edit_estate_nbRooms))
            .perform(
                scrollTo(),
                ViewActions.replaceText("5"),
                ViewActions.closeSoftKeyboard()
            )

        onView(withId(R.id.add_edit_estate_nbBedrooms))
            .perform(
                scrollTo(),
                ViewActions.replaceText("2"),
                ViewActions.closeSoftKeyboard()
            )

        onView(withId(R.id.add_edit_estate_nbBathrooms))
            .perform(
                scrollTo(),
                ViewActions.replaceText("1"),
                ViewActions.closeSoftKeyboard()
            )

        onView(withId(R.id.add_edit_estate_check_park))
            .perform(scrollTo(), click())

        onView(withId(R.id.add_edit_estate_check_school))
            .perform(scrollTo(), click())

        onView(withId(R.id.add_edit_estate_description))
            .perform(
                scrollTo(),
                ViewActions.replaceText("This is a description..."),
                ViewActions.closeSoftKeyboard()
            )

        onView(withId(R.id.add_edit_estate_save))
            .perform(click())

        onView(
            withRecyclerView(R.id.list_estate_recycler).atPositionOnView(
                0,
                R.id.item_estate_name
            )
        )
            .check(matches(withText("Apartment in Paris")))
    }

    @Test
    fun test_open_details() {
        test_add_an_estate()

        onView(
            withRecyclerView(R.id.list_estate_recycler)
                .atPositionOnView(0, R.id.item_estate_main)
        )
            .perform(click())

        Thread.sleep(1000)


        // Check if DetailsFragment is displayed
        onView(
            allOf(
                withId(R.id.fragment_estate_details),
                isDisplayed()
            )
        )
    }

    @Test
    fun test_change_currency() {
        test_add_an_estate()

        onView(
            withRecyclerView(R.id.list_estate_recycler).atPositionOnView(
                0,
                R.id.item_estate_price
            )
        )
            .check(matches(withText("750000$")))

        onView(withId(R.id.main_change_currency))
            .perform(click())

        val toFind = 750000

        onView(
            withRecyclerView(R.id.list_estate_recycler).atPositionOnView(
                0,
                R.id.item_estate_price
            )
        )
            .check(matches(withText(toFind.fromDollarToEuro().toString().plus("€"))))

    }

    @Test
    fun test_search_an_estate() {
        test_add_an_estate()

        add_a_second_estate()

        set_search_city_field("Berlin")

        onView(
            withRecyclerView(R.id.list_estate_recycler).atPositionOnView(
                0,
                R.id.item_estate_name
            )
        )
            .check(matches(withText("Apartment in Berlin")))

        set_search_city_field("Paris")

        onView(
            withRecyclerView(R.id.list_estate_recycler).atPositionOnView(
                0,
                R.id.item_estate_name
            )
        )
            .check(matches(withText("Apartment in Paris")))
    }

    private fun add_a_second_estate() {
        onView(withId(R.id.main_list_bt_add))
            .perform(click())

        onView(withId(R.id.add_edit_estate_title))
            .perform(
                scrollTo(),
                ViewActions.replaceText("Apartment in Berlin"),
                ViewActions.closeSoftKeyboard()
            )

        onView(withId(R.id.add_edit_estate_country))
            .perform(
                scrollTo(),
                ViewActions.replaceText("Allemagne"),
                ViewActions.closeSoftKeyboard()
            )

        onView(withId(R.id.add_edit_estate_city))
            .perform(
                scrollTo(),
                ViewActions.replaceText("Berlin"),
                ViewActions.closeSoftKeyboard()
            )

        onView(withId(R.id.add_edit_estate_zip))
            .perform(
                scrollTo(),
                ViewActions.replaceText("10999"),
                ViewActions.closeSoftKeyboard()
            )

        onView(withId(R.id.add_edit_estate_address))
            .perform(
                scrollTo(),
                ViewActions.replaceText("Lausitzer Str. 22"),
                ViewActions.closeSoftKeyboard()
            )

        onView(withId(R.id.add_edit_estate_area))
            .perform(
                scrollTo(),
                ViewActions.replaceText("50"),
                ViewActions.closeSoftKeyboard()
            )

        onView(withId(R.id.add_edit_estate_price))
            .perform(
                scrollTo(),
                ViewActions.replaceText("500000"),
                ViewActions.closeSoftKeyboard()
            )

        onView(withId(R.id.add_edit_estate_nbRooms))
            .perform(
                scrollTo(),
                ViewActions.replaceText("5"),
                ViewActions.closeSoftKeyboard()
            )

        onView(withId(R.id.add_edit_estate_nbBedrooms))
            .perform(
                scrollTo(),
                ViewActions.replaceText("2"),
                ViewActions.closeSoftKeyboard()
            )

        onView(withId(R.id.add_edit_estate_nbBathrooms))
            .perform(
                scrollTo(),
                ViewActions.replaceText("1"),
                ViewActions.closeSoftKeyboard()
            )

        onView(withId(R.id.add_edit_estate_check_shop))
            .perform(scrollTo(), click())

        onView(withId(R.id.add_edit_estate_check_school))
            .perform(scrollTo(), click())

        onView(withId(R.id.add_edit_estate_description))
            .perform(
                scrollTo(),
                ViewActions.replaceText("This is a description..."),
                ViewActions.closeSoftKeyboard()
            )

        onView(withId(R.id.add_edit_estate_save))
            .perform(click())
    }

    private fun set_search_city_field(city: String) {
        onView(withId(R.id.main_search))
            .perform(click())

        onView(withId(R.id.search_estate_city))
            .perform(
                ViewActions.replaceText(city),
                ViewActions.closeSoftKeyboard()
            )

        onView(withId(R.id.search_estate_search))
            .perform(click())
    }
}