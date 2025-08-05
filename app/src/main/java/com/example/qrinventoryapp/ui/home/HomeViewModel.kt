package com.example.qrinventoryapp.ui.home

import androidx.lifecycle.ViewModel
import com.example.qrinventoryapp.data.RoomEntitiesRepository
import com.example.qrinventoryapp.data.UserEntitiesRepository

enum class AppMode {
    CONTROL,
    INVENTORY
}

class HomeViewModel(
    private val userEntitiesRepository: UserEntitiesRepository,
    private val roomEntitiesRepository: RoomEntitiesRepository
) : ViewModel() {
}