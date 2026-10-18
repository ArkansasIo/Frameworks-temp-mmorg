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
import com.arkansasio.rpgmmo.model.Player

@Composable
fun HeroTaskbar(
    player: Player,
    onInventory: () -> Unit,
    onMap: () -> Unit,
    onDungeon: () -> Unit,
    onCharacter: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Card(shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    PixelHeroAvatar()
                    Column(Modifier.weight(1f)) {
                        Text(player.name, style = MaterialTheme.typography.titleLarge)
                        Text("Level ${player.level}  •  ${player.currentNodeId}")
                        ProgressBar("HP", player.hp.coerceAtLeast(0), player.stats.maxHp.coerceAtLeast(1), MaterialTheme.colorScheme.error)
                        ProgressBar("XP", (player.xp % 10000).toInt(), 10000, MaterialTheme.colorScheme.primary)
                    }
                    Text("${player.gold} G", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            TaskButton("Bag", onInventory)
            TaskButton("Map", onMap)
            TaskButton("Dungeon", onDungeon)
            TaskButton("Hero", onCharacter)
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            repeat(6) { slot ->
                Box(Modifier.weight(1f).aspectRatio(1f).background(Color(0xFF182735), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                    Text("${slot + 1}", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

@Composable
private fun ProgressBar(label: String, value: Int, max: Int, color: Color) {
    Column(Modifier.fillMaxWidth().padding(top = 4.dp)) {
        Text("$label $value/$max", style = MaterialTheme.typography.labelSmall)
        LinearProgressIndicator(progress = { (value.toFloat() / max).coerceIn(0f, 1f) }, modifier = Modifier.fillMaxWidth().height(6.dp), color = color)
    }
}

@Composable
private fun TaskButton(label: String, onClick: () -> Unit) {
    Button(onClick = onClick, modifier = Modifier.weight(1f), contentPadding = PaddingValues(horizontal = 4.dp)) { Text(label) }
}

@Composable
private fun PixelHeroAvatar() {
    Box(Modifier.size(52.dp).background(Color(0xFF24384A), RoundedCornerShape(6.dp)), contentAlignment = Alignment.Center) {
        Text("⚔", style = MaterialTheme.typography.headlineSmall)
    }
}
