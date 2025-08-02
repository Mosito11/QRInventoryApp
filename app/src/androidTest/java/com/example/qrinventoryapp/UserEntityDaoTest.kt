package com.example.qrinventoryapp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.qrinventoryapp.data.InventoryDatabase
import com.example.qrinventoryapp.data.UserEntityDao
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
class UserEntityDaoTest {

    private lateinit var userEntityDao: UserEntityDao
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
        userEntityDao = inventoryDatabase.userEntityDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        inventoryDatabase.close()
    }

    @Test
    fun daoGetAll_returnsUsersCount() = runBlocking {
        val allUserEntities = userEntityDao.getAllUserEntities().first()
        assertTrue(allUserEntities.count() > 0)
        assertEquals(66, allUserEntities.count())
    }

    @Test
    fun daoGetOneById_returnsCorrectUser() = runBlocking {
        val testUserEntityId = 55
        val testUserEntity = userEntityDao.getUserEntityById(testUserEntityId).first()
        assertNotNull(testUserEntity)
        assertEquals(testUserEntityId, testUserEntity.id)
        assertEquals("Mainx Oskar", testUserEntity.name)
    }

}

