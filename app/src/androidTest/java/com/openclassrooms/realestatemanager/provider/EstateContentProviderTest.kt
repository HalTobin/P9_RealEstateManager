package com.openclassrooms.realestatemanager.provider

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import androidx.test.rule.provider.ProviderTestRule
import com.openclassrooms.realestatemanager.data.InMemoryEstateDatabase
import com.openclassrooms.realestatemanager.di.FakeDataModule
import com.openclassrooms.realestatemanager.di.FragmentModule
import com.openclassrooms.realestatemanager.di.ViewModelModule
import junit.framework.Assert.*
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.koin.core.context.loadKoinModules

class EstateContentProviderTest {

    lateinit var mProviderRule: ProviderTestRule

    @Before
    fun setUp() {
        loadKoinModules(
            listOf(
                ViewModelModule.viewModelModule,
                FakeDataModule.fakeDataModule,
                FragmentModule.fragmentsModule
            )
        )

        mProviderRule =
            ProviderTestRule.Builder(
                EstateContentProvider::class.java,
                EstateContentProvider.AUTHORITY
            ).build()
    }

    @Test
    fun test_update() {
        // Insert an item
        insert()

        val contentValuesToUpdate = ContentValues(2)
        contentValuesToUpdate.put("id", InMemoryEstateDatabase.estates[0].id)
        contentValuesToUpdate.put("title", InMemoryEstateDatabase.estates[1].title)

        // Update an item
        val affectedRows: Int =
            mProviderRule.resolver.update(
                EstateContentProvider.URI_ITEM,
                contentValuesToUpdate,
                null
            )
        // Check how many items were affected
        assertEquals(1, affectedRows)

        val cursor: Cursor? = mProviderRule.resolver.query(
            ContentUris.withAppendedId(
                EstateContentProvider.URI_ITEM,
                1
            ), null, null, null, null
        )

        // Check if the field has been correctly updated
        assertThat(cursor!!.moveToFirst(), `is`(true))
        assertThat(
            cursor.getString(cursor.getColumnIndexOrThrow("title")),
            `is`(InMemoryEstateDatabase.estates[1].title)
        )
    }

    @Test
    fun insert_and_get_item() {
        // Add an item
        insert()
        // Test if the item has been correctly added
        val cursor: Cursor? = mProviderRule.resolver.query(
            ContentUris.withAppendedId(
                EstateContentProvider.URI_ITEM,
                1
            ), null, null, null, null
        )
        assertThat(cursor, notNullValue())
        assertThat(cursor!!.count, `is`(1))
        assertThat(cursor.moveToFirst(), `is`(true))
        assertThat(
            cursor.getString(cursor.getColumnIndexOrThrow("title")),
            `is`(InMemoryEstateDatabase.estates[0].title)
        )
    }

    @Test
    fun verify_content_provider_contract_works() {
        val resolver: ContentResolver = mProviderRule.resolver

        val testContentValues = ContentValues(22)
        testContentValues.put("id", InMemoryEstateDatabase.estates[0].id)

        // perform some database (or other) operations
        val uri: Uri? = resolver.insert(EstateContentProvider.URI_ITEM, testContentValues)
        // perform some assertions on the resulting URI
        assertNotNull(uri)
    }

    // Insert an item in the database through the ContentProdivider
    private fun insert() {
        val resolver: ContentResolver = mProviderRule.resolver

        val testContentValues = ContentValues(22)
        testContentValues.put("id", InMemoryEstateDatabase.estates[0].id)
        testContentValues.put("type", InMemoryEstateDatabase.estates[0].type)
        testContentValues.put("title", InMemoryEstateDatabase.estates[0].title)
        testContentValues.put("address", InMemoryEstateDatabase.estates[0].address)
        testContentValues.put("city", InMemoryEstateDatabase.estates[0].city)
        testContentValues.put("zipCode", InMemoryEstateDatabase.estates[0].zipCode)
        testContentValues.put("country", InMemoryEstateDatabase.estates[0].country)
        testContentValues.put("xCoordinate", InMemoryEstateDatabase.estates[0].xCoordinate)
        testContentValues.put("yCoordinate", InMemoryEstateDatabase.estates[0].yCoordinate)
        testContentValues.put("priceDollar", InMemoryEstateDatabase.estates[0].priceDollar)
        testContentValues.put("area", InMemoryEstateDatabase.estates[0].area)
        testContentValues.put("nbRooms", InMemoryEstateDatabase.estates[0].nbRooms)
        testContentValues.put("nbBathrooms", InMemoryEstateDatabase.estates[0].nbBathrooms)
        testContentValues.put("nbBedrooms", InMemoryEstateDatabase.estates[0].nbBedrooms)
        testContentValues.put("nearbySchool", InMemoryEstateDatabase.estates[0].nearbySchool)
        testContentValues.put("nearbyShop", InMemoryEstateDatabase.estates[0].nearbyShop)
        testContentValues.put("nearbyPark", InMemoryEstateDatabase.estates[0].nearbyPark)
        testContentValues.put("sold", InMemoryEstateDatabase.estates[0].sold)
        testContentValues.put("entryDate", InMemoryEstateDatabase.estates[0].entryDate)
        testContentValues.put("soldDate", InMemoryEstateDatabase.estates[0].soldDate)
        testContentValues.put("agentId", InMemoryEstateDatabase.estates[0].agentId)
        testContentValues.put("description", InMemoryEstateDatabase.estates[0].description)

        // insert an item
        resolver.insert(EstateContentProvider.URI_ITEM, testContentValues)

    }
}