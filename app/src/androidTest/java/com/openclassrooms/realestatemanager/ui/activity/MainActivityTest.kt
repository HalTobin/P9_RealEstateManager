package com.openclassrooms.realestatemanager.ui.activity

import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.GrantPermissionRule
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.di.FakeDataModule
import com.openclassrooms.realestatemanager.di.FragmentModule
import com.openclassrooms.realestatemanager.di.ViewModelModule
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.Description
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Before
    fun setUp() =
        loadKoinModules(
            listOf(
                ViewModelModule.viewModelModule,
                FakeDataModule.fakeDataModule,
                FragmentModule.fragmentsModule
            )
        )

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

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
    fun test_navigation_to_add_estate() {
        onView(withId(R.id.main_list_bt_add))
            .perform(click())

        Intents.intended(hasComponent(AddEditEstateActivity::class.java.name))
    }

    /*@Test
    fun test_add_an_estate() {
        test_navigation_to_add_estate()

        onView(withId(R.id.add_edit_estate_title))
            .perform(
            scrollTo(),
            ViewActions.replaceText("Title"),
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

        onView(withId(R.id.add_edit_estate_find))
            .perform(click())

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

        onView(withId(R.id.add_edit_estate_agent))
            .perform(click())

        val selectionText = "Clara SAAVEDRA"
        //onData(allOf(`is`(instanceOf(String::class.java)), `is`(selectionText))).perform(click())
        //onView(withId(R.id.add_edit_estate_agent)).check(matches(withSpinnerText(containsString(selectionText))))

        onView(withId(R.id.add_edit_estate_description))
            .perform(
                scrollTo(),
                ViewActions.replaceText("This is a description..."),
                ViewActions.closeSoftKeyboard()
            )

        onView(withId(R.id.add_edit_estate_save))
            .perform(click())

    }*/

}