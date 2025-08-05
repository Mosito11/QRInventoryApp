package com.example.qrinventoryapp.ui.scan

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.qrinventoryapp.data.IncorrectItemsRepository
import com.example.qrinventoryapp.data.ItemsRepository

class ScanViewModel(
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: ItemsRepository,
    private val incorrectItemsRepository: IncorrectItemsRepository
) : ViewModel() {
}