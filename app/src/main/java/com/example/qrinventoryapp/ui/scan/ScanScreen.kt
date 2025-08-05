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
import com.example.qrinventoryapp.ui.home.AppMode
import com.example.qrinventoryapp.ui.home.HomeViewModel
import com.example.qrinventoryapp.ui.navigation.NavigationHelper

object ScanScreenDestination : NavigationHelper {
    override val route = "scan"

    const val modeArg = "mode"
    const val userIdArg = "userId"
    const val roomIdArg = "roomId"

    val routeWithArgs = "$route?$modeArg={$modeArg}&$userIdArg={$userIdArg}&$roomIdArg={$roomIdArg}"

    override val titleRes = R.string.scanscreen_title

    fun createRoute(mode: AppMode, selectedUserId: Int? = null, selectedRoomId: Int? = null): String {
        val modePart = "$route?$modeArg=${mode.name}"
        val userPart = selectedUserId?.let { "&$userIdArg=$it" } ?: ""
        val roomPart = selectedRoomId?.let { "&$roomIdArg=$it" } ?: ""
        return modePart + userPart + roomPart
    }
}

@Composable
fun ScanScreen(
    navigateBack: () -> Unit,
    saveIncorrectItem: () -> Unit,
    newScan: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ScanViewModel = viewModel(factory = AppViewModelProvider.Factory)

) {
}