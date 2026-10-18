package com.arkansasio.rpgmmo.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkansasio.rpgmmo.model.Player

@Composable fun RpgMmoScreen(player:Player,onXp:()->Unit,onItem:()->Unit,onPvp:()->Unit,onQuest:()->Unit,onAdvance:()->Unit,onQueue:()->Unit){
    Scaffold(topBar={TopAppBar(title={Text("RPG MMO Command")})}){pad->
        LazyColumn(Modifier.fillMaxSize().padding(pad).padding(16.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){
            item{Text(player.name,style=MaterialTheme.typography.headlineMedium);Text("Level ${player.level} • XP ${player.xp} • HP ${player.hp}/${player.stats.maxHp}")}
            item{StatsCard(player)}
            item{Button(onClick=onXp,Modifier.fillMaxWidth()){Icon(Icons.Default.Add,null);Spacer(Modifier.width(8.dp));Text("Grant 500 XP")}}
            item{Button(onClick=onItem,Modifier.fillMaxWidth()){Icon(Icons.Default.ShoppingBag,null);Spacer(Modifier.width(8.dp));Text("Add Health Potion")}}
            item{Button(onClick=onPvp,Modifier.fillMaxWidth()){Text(if(player.pvpEnabled)"Disable PvP" else "Enable PvP")}}
            item{Button(onClick=onQueue,Modifier.fillMaxWidth()){Text("Queue Arena Match")}}
            item{Button(onClick=onQuest,Modifier.fillMaxWidth()){Text("Create First Hunt Quest")}}
            item{Button(onClick=onAdvance,Modifier.fillMaxWidth()){Text("Advance Quest")}}
            item{Text("Inventory",style=MaterialTheme.typography.titleLarge);player.inventory.forEach{Text("• ${it.name} x${it.quantity}")}}
            item{Text("Quests",style=MaterialTheme.typography.titleLarge);player.quests.forEach{Text("• ${it.name}: ${it.progress}/${it.required}${if(it.completed)" ✓" else ""}")}}
        }
    }
}

@Composable private fun StatsCard(p:Player){Card(Modifier.fillMaxWidth()){Column(Modifier.padding(16.dp)){Text("Core Stats",style=MaterialTheme.typography.titleLarge);Text("STR ${p.stats.str}  DEX ${p.stats.dex}  CON ${p.stats.con}  INT ${p.stats.int}");Text("WIS ${p.stats.wis}  VIT ${p.stats.vit}  SPI ${p.stats.spi}  LCK ${p.stats.lck}");Text("ATK ${p.stats.attackPower}  DEF ${p.stats.defense}  MAX HP ${p.stats.maxHp}")}}}
