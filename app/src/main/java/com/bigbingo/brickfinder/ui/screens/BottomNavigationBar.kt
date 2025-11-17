package com.bigbingo.brickfinder.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun BottomNavigationBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        NavigationBar(containerColor = Color.White) {
            NavigationBarItem(
                icon = { Icon(Icons.Default.FavoriteBorder, contentDescription = "Wanted List") },
                label = { Text("Wanted List") },
                selected = selectedIndex == 0,
                onClick = { onItemSelected(0) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color( 0xFF0788CA),
                    unselectedIconColor = Color.DarkGray,
                    selectedTextColor = Color( 0xFF0788CA),
                    unselectedTextColor = Color.DarkGray,
                    indicatorColor = Color( 0xFF0788CA).copy(alpha = 0.25f)
                )
            )
            NavigationBarItem(
                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                label = { Text("Home") },
                selected = selectedIndex == 1,
                onClick = { onItemSelected(1) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color( 0xFF0788CA),
                    unselectedIconColor = Color.DarkGray,
                    selectedTextColor = Color( 0xFF0788CA),
                    unselectedTextColor = Color.DarkGray,
                    indicatorColor = Color( 0xFF0788CA).copy(alpha = 0.25f)
                )
            )
        }
    }
}
