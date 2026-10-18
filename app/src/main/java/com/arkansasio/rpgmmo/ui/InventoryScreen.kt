package com.arkansasio.rpgmmo.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkansasio.rpgmmo.model.*

@Composable
fun InventoryScreen(bag: InventoryBag, definitions: Map<String, ItemDefinition>, onUse: (InventorySlot) -> Unit) {
    Column(Modifier.fillMaxSize().padding(12.dp)) {
        Text("Inventory", style = MaterialTheme.typography.headlineMedium)
        Text("${bag.slots.count { it.itemId != null }} / ${bag.capacity} occupied")
        Spacer(Modifier.height(8.dp))
        LazyVerticalGrid(columns = GridCells.Fixed(5), modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(6.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(bag.slots) { slot ->
                val item = slot.itemId?.let { definitions[it] }
                OutlinedButton(onClick = { onUse(slot) }, modifier = Modifier.aspectRatio(1f), contentPadding = PaddingValues(4.dp)) {
                    Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                        Text(if (item == null) "·" else item.name.take(8), style = MaterialTheme.typography.labelSmall)
                        if (slot.quantity > 1) Text("x${slot.quantity}", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}
