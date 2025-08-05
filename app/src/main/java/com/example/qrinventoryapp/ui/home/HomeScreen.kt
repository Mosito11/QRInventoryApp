package com.example.qrinventoryapp.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.qrinventoryapp.R
import com.example.qrinventoryapp.ui.AppViewModelProvider
import com.example.qrinventoryapp.ui.navigation.NavigationHelper

object HomeDestination : NavigationHelper {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
}