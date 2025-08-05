package com.example.qrinventoryapp.ui.scan

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.qrinventoryapp.data.IncorrectItemsRepository
import com.example.qrinventoryapp.data.ItemsRepository
import com.example.qrinventoryapp.ui.home.AppMode

class ScanViewModel(
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: ItemsRepository,
    private val incorrectItemsRepository: IncorrectItemsRepository
) : ViewModel() {

    val mode = AppMode.valueOf(
        savedStateHandle[ScanScreenDestination.modeArg] ?: AppMode.CONTROL.name
    )
    val selectedUserId: Int? = savedStateHandle[ScanScreenDestination.userIdArg]
    val selectedRoomId: Int? = savedStateHandle[ScanScreenDestination.roomIdArg]

}