package com.example.qrinventoryapp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.qrinventoryapp.data.InventoryDatabase
import com.example.qrinventoryapp.data.RoomEntityDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RoomEntityDaoTest {

    private lateinit var roomEntityDao: RoomEntityDao
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
        roomEntityDao = inventoryDatabase.roomEntityDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        inventoryDatabase.close()
    }

    @Test
    fun daoGetAll_returnsRoomEntitiesCount() = runBlocking {
        val allRoomEntities = roomEntityDao.getAllRoomEntities().first()
        assertTrue(allRoomEntities.count() > 0)
        assertEquals(38, allRoomEntities.count())
    }

    @Test
    fun daoGetOneById_returnsCorrectRoom() = runBlocking {
        val testRoomEntityId = 37
        val testRoomEntity = roomEntityDao.getRoomEntityById(testRoomEntityId).first()
        assertNotNull(testRoomEntity)
        assertEquals(testRoomEntityId, testRoomEntity.id)
        assertEquals("kabinet fyziky", testRoomEntity.name)
    }
}