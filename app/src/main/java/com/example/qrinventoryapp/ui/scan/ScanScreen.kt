package com.example.qrinventoryapp.ui.scan

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.qrinventoryapp.R
import com.example.qrinventoryapp.data.IncorrectItemsRepository
import com.example.qrinventoryapp.data.ItemsRepository
import com.example.qrinventoryapp.data.RoomEntitiesRepository
import com.example.qrinventoryapp.data.UserEntitiesRepository
import com.example.qrinventoryapp.ui.AppViewModelProvider
import com.example.qrinventoryapp.ui.home.HomeViewModel
import com.example.qrinventoryapp.ui.navigation.NavigationHelper

object ScanScreenDestination : NavigationHelper {
    override val route = "scan"
    override val titleRes = R.string.scanscreen_title
}

@Composable
fun ScanScreen(
    modifier: Modifier = Modifier,
    viewModel: ScanViewModel = viewModel(factory = AppViewModelProvider.Factory)

) {
}