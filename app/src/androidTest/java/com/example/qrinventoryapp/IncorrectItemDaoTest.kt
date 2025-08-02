package com.example.qrinventoryapp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.qrinventoryapp.data.IncorrectItem
import com.example.qrinventoryapp.data.IncorrectItemDao
import com.example.qrinventoryapp.data.InventoryDatabase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class IncorrectItemDaoTest {

    private lateinit var incorrectItemDao: IncorrectItemDao
    private lateinit var inventoryDatabase: InventoryDatabase

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()

        val testDbFile = context.getDatabasePath("test_inventory.db")
        if (testDbFile.exists()) testDbFile.delete()

        context.assets.open("database/inventory.db").use { inputStream ->
            testDbFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }

        inventoryDatabase = Room.databaseBuilder(
            context,
            InventoryDatabase::class.java,
            "test_inventory.db"
        )
            .allowMainThreadQueries()
            .build()

        //inventoryDatabase = Room.inMemoryDatabaseBuilder(context, InventoryDatabase::class.java)
        //    .allowMainThreadQueries()
        //    .build()
        incorrectItemDao = inventoryDatabase.incorrectItemDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        inventoryDatabase.close()
    }

    private var incorrectItem1 = IncorrectItem(1, "ABC123", "11.5.1980", 1, 2, 1, 1)
    private var incorrectItem2 = IncorrectItem(2, "DEF123", "11.6.1981", 1, 1,  2,  1)

    private suspend fun addOneIncorrectItemToDb() {
        incorrectItemDao.insert(incorrectItem1)
    }

    private suspend fun addTwoIncorrectItemsToDb() {
        incorrectItemDao.insert(incorrectItem1)
        incorrectItemDao.insert(incorrectItem2)
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsIncorrectItemIntoDB() = runBlocking {
        addOneIncorrectItemToDb()
        val allIncorrectItems = incorrectItemDao.getAllIncorrectItems().first()
        assertEquals(allIncorrectItems[0], incorrectItem1)
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsTwoIncorrectItemsIntoDB() = runBlocking {
        addTwoIncorrectItemsToDb()
        val allIncorrectItems = incorrectItemDao.getAllIncorrectItems().first()
        assertEquals(allIncorrectItems[0], incorrectItem1)
        assertEquals(allIncorrectItems[1], incorrectItem2)
    }

    @Test
    @Throws(Exception::class)
    fun daoDeleteItems_deletesAllIncorrectItemsFromDB() = runBlocking {
        addTwoIncorrectItemsToDb()
        incorrectItemDao.deleteAll()

        val allIncorrectItems = incorrectItemDao.getAllIncorrectItems().first()
        assertTrue(allIncorrectItems.isEmpty())
    }

    @Test
    fun daoGetAll_onEmptyDb_returnsEmptyList() = runBlocking {
        val allIncorrectItems = incorrectItemDao.getAllIncorrectItems().first()
        assertTrue(allIncorrectItems.isEmpty())
    }
}

