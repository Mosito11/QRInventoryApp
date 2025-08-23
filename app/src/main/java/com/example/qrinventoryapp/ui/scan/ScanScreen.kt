package com.example.qrinventoryapp.ui.scan

import android.R.attr.maxLines
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import com.example.qrinventoryapp.QRInventoryTopAppBar
import com.example.qrinventoryapp.R
import com.example.qrinventoryapp.ui.AppViewModelProvider
import com.example.qrinventoryapp.ui.home.AppMode
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
    viewModel: ScanViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navBackStackEntry: NavBackStackEntry
) {
    val mode = navBackStackEntry.arguments
        ?.getString(ScanScreenDestination.modeArg)
        ?.let { AppMode.valueOf(it) }
        ?: AppMode.CONTROL

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            QRInventoryTopAppBar(title = stringResource(R.string.topbar_title) + " " + mode.name)
        }
    ) { innerPadding ->

        ScanScreenContent(
            uiState = uiState,
            selectedMode = viewModel.selectedMode,
            navigateBack = navigateBack,
            saveIncorrectItem = saveIncorrectItem,
            newScan = newScan,
            modifier = modifier,
            contentPadding = innerPadding
        )
    }
}

@Composable
fun ScanScreenContent(
    uiState: ScanUiState,
    selectedMode: AppMode,
    navigateBack: () -> Unit,
    saveIncorrectItem: () -> Unit,
    newScan: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // placeholder pre kameru
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2.5f)
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "TODO: Camera Preview")
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.8f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
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
                    true -> Icon(Icons.Default.Check, null, tint = Color.Green, modifier = Modifier.size(20.dp))
                    false -> Icon(Icons.Default.Close, null, tint = Color.Red, modifier = Modifier.size(20.dp))
                    null -> Spacer(Modifier.size(24.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                Text(uiState.roomNameFromDB ?: stringResource(R.string.no_room_found))
                Spacer(Modifier.width(4.dp))
                when (uiState.roomMatch) {
                    true -> Icon(Icons.Default.Check, null, tint = Color.Green, modifier = Modifier.size(20.dp))
                    false -> Icon(Icons.Default.Close, null, tint = Color.Red, modifier = Modifier.size(20.dp))
                    null -> Spacer(Modifier.size(24.dp))
                }
            }

            if (uiState.errorTextId != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(id = uiState.errorTextId),
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.7f),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = navigateBack,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Text(
                    stringResource(R.string.back),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Button(
                onClick = saveIncorrectItem,
                enabled = selectedMode == AppMode.INVENTORY &&
                        (uiState.userMatch == false || uiState.roomMatch == false),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Text(
                    stringResource(R.string.save_incorrect_item),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Button(
                onClick = newScan,
                enabled = uiState.qr != null || uiState.errorTextId != null,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Text(
                    stringResource(R.string.new_scan),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun ScanScreenPreview() {
    val fakeUiState = ScanUiState(
        qr = "QR123456789",
        userNameFromDB = "Andrej Babis",
        roomNameFromDB = "Strakova Akademie",
        userMatch = true,
        roomMatch = false,
        errorTextId = null
    )

    val fakeMode = AppMode.INVENTORY

    Scaffold(
        topBar = {
            QRInventoryTopAppBar(title = stringResource(R.string.topbar_title) + " " + fakeMode.name)
        }
    ) { innerPadding ->
        ScanScreenContent(
            uiState = fakeUiState,
            selectedMode = AppMode.INVENTORY,
            navigateBack = {},
            saveIncorrectItem = {},
            newScan = {},
            contentPadding = innerPadding
        )
    }
}
