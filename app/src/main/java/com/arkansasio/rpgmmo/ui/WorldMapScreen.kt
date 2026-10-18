package com.arkansasio.rpgmmo.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arkansasio.rpgmmo.model.*

@Composable
fun WorldMapScreen(map: WorldMap, zones: List<Zone>, player: Player, onTravel: (String) -> Unit) {
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(map.name, style = MaterialTheme.typography.headlineMedium)
        Text("Current location: ${player.currentNodeId}")
        zones.forEach { zone ->
            val unlocked = player.level >= zone.recommendedLevel
            Card(Modifier.fillMaxWidth()) {
                Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(42.dp).background(if (unlocked) Color(0xFF214B3B) else Color(0xFF3A3030), RoundedCornerShape(6.dp)), contentAlignment = Alignment.Center) {
                        Text(if (unlocked) "◆" else "🔒")
                    }
                    Column(Modifier.weight(1f).padding(start = 10.dp)) {
                        Text(zone.name, style = MaterialTheme.typography.titleMedium)
                        Text("Recommended level ${zone.recommendedLevel} • ${zone.width}×${zone.height}")
                    }
                    Button(enabled = unlocked, onClick = { onTravel(zone.nodeIds.firstOrNull() ?: player.currentNodeId) }) { Text("Travel") }
                }
            }
        }
    }
}
