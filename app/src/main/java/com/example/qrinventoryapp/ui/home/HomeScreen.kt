package com.example.qrinventoryapp.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.qrinventoryapp.R
import com.example.qrinventoryapp.data.RoomEntity
import com.example.qrinventoryapp.data.UserEntity
import com.example.qrinventoryapp.ui.AppViewModelProvider
import com.example.qrinventoryapp.ui.navigation.NavigationHelper

object HomeDestination : NavigationHelper {
    override val route = "home"
    override val titleRes = R.string.homescreen_title
}

@Composable
fun HomeScreen(
    navigateToScan: (AppMode, Int?, Int?) -> Unit,
    navigateToQuit: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    var showUserDialog by remember { mutableStateOf(false) }
    var showRoomDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        //Mode Dropdown
        ModeDropdown(
            selectedMode = uiState.selectedMode,
            onModeSelected = viewModel::selectMode
        )

        //Select contact
        if (uiState.selectedMode == AppMode.INVENTORY) {
            Button(
                onClick = { showUserDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(uiState.availableUsers.firstOrNull { it.id == uiState.selectedUserId }?.name ?: stringResource(R.string.user_selection))
            }

            Button(
                onClick = { showRoomDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(uiState.availableRooms.firstOrNull { it.id == uiState.selectedRoomId }?.name ?: stringResource(R.string.room_selection))
            }
        }

        //Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = navigateToQuit) {
                Text(stringResource(R.string.quit))
            }
            Button(
                onClick = { navigateToScan(uiState.selectedMode, uiState.selectedUserId, uiState.selectedRoomId) },
                enabled = uiState.isScanEnabled
            ) {
                Text(stringResource(R.string.scan))
            }
        }

        if (showUserDialog) {
            EntitySelectDialog(
                title = stringResource(R.string.user_selection),
                items = uiState.availableUsers.map { it.id to it.name },
                onSelect = { viewModel.selectUser(it) },
                onDismiss = { showUserDialog = false }
            )
        }

        // Room dialog
        if (showRoomDialog) {
            EntitySelectDialog(
                title = stringResource(R.string.room_selection),
                items = uiState.availableRooms.map { it.id to it.name },
                onSelect = { viewModel.selectRoom(it) },
                onDismiss = { showRoomDialog = false }
            )
        }
    }
}

@Composable
private fun ModeDropdown(
    selectedMode: AppMode,
    onModeSelected: (AppMode) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(
            onClick = { expanded = true },
            modifier = modifier
        ) {
            Text(selectedMode.name)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            AppMode.entries.forEach { mode ->
                DropdownMenuItem(
                    text = { Text(mode.name) },
                    onClick = {
                        onModeSelected(mode)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun EntitySelectDialog(
    title: String,
    items: List<Pair<Int, String>>,
    onSelect: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredItems = items.filter { it.second.contains(searchQuery, ignoreCase = true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text(stringResource(R.string.search)) },
                    modifier = Modifier.fillMaxWidth()
                )
                LazyColumn {
                    items(filteredItems) { (id, name) ->
                        Text(
                            text = name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelect(id)
                                    onDismiss()
                                }
                                .padding(8.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.search))
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val fakeUiState = HomeUiState(
        selectedMode = AppMode.INVENTORY,
        selectedUserId = null,
        selectedRoomId = null,
        availableUsers = listOf(
            UserEntity(1, "Peter Parker"),
            UserEntity(2, "Tony Stark"),
            UserEntity(3, "Natasha Romanoff")
        ),
        availableRooms = listOf(
            RoomEntity(1, "Server Room"),
            RoomEntity(2, "Meeting Room"),
            RoomEntity(3, "Storage")
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Application mode: " + fakeUiState.selectedMode.name) })
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    modifier = Modifier
                        //.fillMaxSize()
                        .width(300.dp)
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(100.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.weight(1f))

                    ModeDropdown(
                        selectedMode = fakeUiState.selectedMode,
                        onModeSelected = { },
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (fakeUiState.selectedMode == AppMode.INVENTORY) {
                        Button(
                            onClick = { },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                fakeUiState.availableUsers.firstOrNull { it.id == fakeUiState.selectedUserId }?.name
                                    ?: stringResource(R.string.user_selection)
                            )
                        }

                        Button(
                            onClick = { },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                fakeUiState.availableRooms.firstOrNull { it.id == fakeUiState.selectedRoomId }?.name
                                    ?: stringResource(R.string.room_selection)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(onClick = {}, modifier = Modifier.weight(1f)) {
                            Text("Quit")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {},
                            modifier = Modifier.weight(1f),
                            enabled = fakeUiState.isScanEnabled
                        ) {
                            Text("Scan")
                        }
                    }
                }
            }
        }
    )
}
