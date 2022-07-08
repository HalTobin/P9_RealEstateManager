package com.openclassrooms.realestatemanager.provider

import io.mockk.clearAllMocks
import org.junit.After
import org.junit.Test

//TODO - See how to test the content provider
class EstateContentProviderTest {

    //lateinit var mProviderRule: ProviderTestRule

    /*@Before
    fun setUp() {
        mProviderRule: ProviderTestRule =
            Builder(MyContentProvider::class.java, MyContentProvider.AUTHORITY).build()
    }*/

    @After
    fun tearDown() = clearAllMocks()

    /*@Test
    fun verifyContentProviderContractWorks() {
        val resolver: ContentResolver = mProviderRule.getResolver()
        // perform some database (or other) operations
        val uri: Uri? = resolver.insert(testUrl, testContentValues)
        // perform some assertions on the resulting URI
        assertNotNull(uri)
    }

    @Test
    fun onCreate() {
    }

    fun testQuery() {
        val provider: ContentProvider = getProvider()
        val uri: Uri = ProfileContract.CONTENT_URI
        var cursor: Cursor? = provider.query(uri, null, null, null, null)
        assertNotNull(cursor)
        cursor = null
        try {
            cursor = provider.query(Uri.parse("definitelywrong"), null, null, null, null)
            // we're wrong if we get until here!
            fail()
        } catch (e: IllegalArgumentException) {
            assertTrue(true)
        }
    }*/

    @Test
    fun query() {
    }

    @Test
    fun getType() {
    }

    @Test
    fun insert() {
    }

    @Test
    fun delete() {
    }

    @Test
    fun update() {
    }
}