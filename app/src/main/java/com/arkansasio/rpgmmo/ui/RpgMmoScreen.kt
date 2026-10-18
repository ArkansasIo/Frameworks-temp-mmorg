package com.arkansasio.rpgmmo.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkansasio.rpgmmo.model.Player

@Composable
fun RpgMmoScreen(player:Player,onXp:()->Unit,onItem:()->Unit,onPvp:()->Unit,onQuest:()->Unit,onAdvance:()->Unit,onQueue:()->Unit,onGuild:()->Unit,onParty:()->Unit,onSkill:()->Unit,onTrain:()->Unit,onTalent:()->Unit,onFaction:()->Unit,onEquip:()->Unit){
    Scaffold(topBar={TopAppBar(title={Text("RPG MMO Command")})}){pad->
        LazyColumn(Modifier.fillMaxSize().padding(pad).padding(16.dp),verticalArrangement=Arrangement.spacedBy(10.dp)){
            item{Text(player.name,style=MaterialTheme.typography.headlineMedium);Text("Level ${player.level} • XP ${player.xp} • HP ${player.hp}/${player.stats.maxHp} • Gold ${player.gold}")}
            item{StatsCard(player)}
            item{ActionButton("Grant 500 XP",onXp)}
            item{ActionButton("Add Health Potion",onItem)}
            item{ActionButton(if(player.pvpEnabled)"Disable PvP" else "Enable PvP",onPvp)}
            item{ActionButton("Queue Arena Match",onQueue)}
            item{ActionButton("Create First Hunt Quest",onQuest)}
            item{ActionButton("Advance Quest",onAdvance)}
            item{ActionButton("Create Guild",onGuild)}
            item{ActionButton("Create Party",onParty)}
            item{ActionButton("Learn Power Strike",onSkill)}
            item{ActionButton("Train Power Strike",onTrain)}
            item{ActionButton("Invest Strength Talent",onTalent)}
            item{ActionButton("Improve Alliance Standing",onFaction)}
            item{ActionButton("Equip Starter Sword",onEquip)}
            item{Section("Inventory");player.inventory.forEach{Text("• ${it.name} x${it.quantity}")}}
            item{Section("Equipment");player.equipment.forEach{Text("• ${it.slot}: ${it.itemName} +${it.power}")}}
            item{Section("Skills");player.skills.forEach{Text("• ${it.name} Rank ${it.rank}/${it.maxRank}")}}
            item{Section("Talents");player.talents.forEach{Text("• ${it.name} Rank ${it.rank}/${it.maxRank}")}}
            item{Section("Factions");player.factions.forEach{Text("• ${it.name}: ${it.standing}")}}
            item{Section("Quests");player.quests.forEach{Text("• ${it.name}: ${it.progress}/${it.required}${if(it.completed)" ✓" else ""}")}}
            item{Section("Guild");Text(player.guild?.let{"${it.name} • ${it.members.size} members"} ?: "No guild")}
            item{Section("Party");Text(player.party?.let{"${it.id} • ${it.members.size} members"} ?: "No party")}
            item{Section("World");Text("Current node: ${player.currentNodeId}")}
        }
    }
}

@Composable private fun ActionButton(label:String,onClick:()->Unit){Button(onClick=onClick,Modifier.fillMaxWidth()){Text(label)}}
@Composable private fun Section(title:String){Text(title,style=MaterialTheme.typography.titleLarge)}
@Composable private fun StatsCard(p:Player){Card(Modifier.fillMaxWidth()){Column(Modifier.padding(16.dp)){Text("Core Stats",style=MaterialTheme.typography.titleLarge);Text("STR ${p.stats.str}  DEX ${p.stats.dex}  CON ${p.stats.con}  INT ${p.stats.int}");Text("WIS ${p.stats.wis}  VIT ${p.stats.vit}  SPI ${p.stats.spi}  LCK ${p.stats.lck}");Text("WIL ${p.stats.wil}  CHA ${p.stats.cha}");Text("ATK ${p.stats.attackPower}  DEF ${p.stats.defense}  MAGIC ${p.stats.magicPower}  CRIT ${p.stats.critChance}%")}}}
