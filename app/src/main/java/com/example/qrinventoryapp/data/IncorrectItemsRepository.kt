package com.example.qrinventoryapp.data

import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

interface IncorrectItemsRepository {

    suspend fun insert(incorrectItem: IncorrectItem)

    fun getAllIncorrectItemsStream(): Flow<List<IncorrectItem>>

    suspend fun deleteAllIncorrectItems()
}