package com.arkansasio.rpgmmo.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkansasio.rpgmmo.domain.DungeonEngine
import com.arkansasio.rpgmmo.model.*

@Composable
fun DungeonScreen(dungeons: List<Dungeon>, player: Player, onEnter: (Dungeon) -> Unit) {
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Dungeons", style = MaterialTheme.typography.headlineMedium)
        dungeons.forEach { dungeon ->
            val ready = player.level >= dungeon.levelMin
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp)) {
                    Text(dungeon.name, style = MaterialTheme.typography.titleLarge)
                    Text("Levels ${dungeon.levelMin}-${dungeon.levelMax} • ${dungeon.rooms} rooms • ${dungeon.recommendedPlayers} players")
                    Text("Recommended power ${DungeonEngine.recommendedPower(dungeon)}")
                    Spacer(Modifier.height(6.dp))
                    Button(enabled = ready, onClick = { onEnter(dungeon) }, modifier = Modifier.fillMaxWidth()) {
                        Text(if (ready) "Enter Dungeon" else "Level ${dungeon.levelMin} Required")
                    }
                }
            }
        }
    }
}
