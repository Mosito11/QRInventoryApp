package com.example.qrinventoryapp.ui.scan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // placeholder pre kameru
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "TODO: Camera Preview")
        }

        // textove polia pre vysledok skenovania
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = uiState.qr ?: stringResource(R.string.no_qr_scanned),
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text(uiState.userNameFromDB ?: stringResource(R.string.no_user_found))
                Spacer(Modifier.width(8.dp))
                when (uiState.userMatch) {
                    true -> Icon(Icons.Default.Check, contentDescription = "Match", tint = Color.Green, modifier = Modifier.size(20.dp))
                    false -> Icon(Icons.Default.Close, contentDescription = "Mismatch", tint = Color.Red, modifier = Modifier.size(20.dp))
                    null -> Spacer(Modifier.size(24.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text(uiState.roomNameFromDB ?: stringResource(R.string.no_room_found))
                Spacer(Modifier.width(8.dp))
                when (uiState.roomMatch) {
                    true -> Icon(Icons.Default.Check, contentDescription = "Match", tint = Color.Green, modifier = Modifier.size(20.dp))
                    false -> Icon(Icons.Default.Close, contentDescription = "Mismatch", tint = Color.Red, modifier = Modifier.size(20.dp))
                    null -> Spacer(Modifier.size(24.dp))
                }
            }

            if (uiState.errorTextId != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(id = uiState.errorTextId!!),
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // buttony
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = navigateBack) {
                Text(stringResource(R.string.back))
            }

            Button(
                onClick = saveIncorrectItem,
                enabled = viewModel.selectedMode == AppMode.INVENTORY &&
                        (uiState.userMatch == false || uiState.roomMatch == false)
            ) {
                Text(stringResource(R.string.save_incorrect_item))
            }

            Button(
                onClick = newScan,
                enabled = uiState.qr != null || uiState.errorTextId != null
            ) {
                Text(stringResource(R.string.new_scan))
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun ScanScreenPreview() {
    val fakeUiState = ScanUiState(
        qr = "QR123456789",
        userNameFromDB = "Ján Novák",
        roomNameFromDB = "Sklad 1",
        userMatch = true,
        roomMatch = false,
        errorTextId = null
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // placeholder pre kameru
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "TODO: Camera Preview")
        }

        // textove polia pre vysledok skenovania
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = fakeUiState.qr ?: stringResource(R.string.no_qr_scanned),
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text(fakeUiState.userNameFromDB ?: stringResource(R.string.no_user_found))
                Spacer(Modifier.width(8.dp))
                when (fakeUiState.userMatch) {
                    true -> Icon(
                        Icons.Default.Check,
                        contentDescription = "Match",
                        tint = Color.Green,
                        modifier = Modifier.size(20.dp)
                    )

                    false -> Icon(
                        Icons.Default.Close,
                        contentDescription = "Mismatch",
                        tint = Color.Red,
                        modifier = Modifier.size(20.dp)
                    )

                    null -> Spacer(Modifier.size(24.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text(fakeUiState.roomNameFromDB ?: stringResource(R.string.no_room_found))
                Spacer(Modifier.width(8.dp))
                when (fakeUiState.roomMatch) {
                    true -> Icon(
                        Icons.Default.Check,
                        contentDescription = "Match",
                        tint = Color.Green,
                        modifier = Modifier.size(20.dp)
                    )

                    false -> Icon(
                        Icons.Default.Close,
                        contentDescription = "Mismatch",
                        tint = Color.Red,
                        modifier = Modifier.size(20.dp)
                    )

                    null -> Spacer(Modifier.size(24.dp))
                }
            }

            if (fakeUiState.errorTextId != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(id = fakeUiState.errorTextId!!),
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // buttony
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = { }) {
                Text(stringResource(R.string.back))
            }

            Button(
                onClick = { },
                enabled = true
            ) {
                Text(stringResource(R.string.save_incorrect_item))
            }

            Button(
                onClick = { },
                enabled = fakeUiState.qr != null || fakeUiState.errorTextId != null
            ) {
                Text(stringResource(R.string.new_scan))
            }
        }
    }
}
