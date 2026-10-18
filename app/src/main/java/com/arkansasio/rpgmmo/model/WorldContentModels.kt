package com.arkansasio.rpgmmo.model

import kotlinx.serialization.Serializable

@Serializable
enum class BiomeType { PLAINS, FOREST, DESERT, TUNDRA, SWAMP, VOLCANIC, MOUNTAIN, COAST, RUINS, ARCANE }

@Serializable
data class Biome(
    val id: String,
    val name: String,
    val type: BiomeType,
    val levelMin: Int,
    val levelMax: Int,
    val resourceIds: List<String> = emptyList(),
    val enemyIds: List<String> = emptyList()
)

@Serializable
data class Zone(
    val id: String,
    val name: String,
    val biomeId: String,
    val recommendedLevel: Int,
    val width: Int = 32,
    val height: Int = 32,
    val nodeIds: List<String> = emptyList(),
    val dungeonIds: List<String> = emptyList(),
    val safeZone: Boolean = false
)

@Serializable
data class MapPoint(
    val id: String,
    val zoneId: String,
    val x: Int,
    val y: Int,
    val label: String,
    val kind: String = "location"
)

@Serializable
data class WorldMap(
    val id: String,
    val name: String,
    val zoneIds: List<String>,
    val points: List<MapPoint> = emptyList()
)

@Serializable
data class Dungeon(
    val id: String,
    val name: String,
    val zoneId: String,
    val levelMin: Int,
    val levelMax: Int,
    val rooms: Int = 5,
    val bossId: String,
    val recommendedPlayers: Int = 4,
    val lootTableId: String = "default"
)

@Serializable
data class DungeonRoom(
    val id: String,
    val dungeonId: String,
    val index: Int,
    val name: String,
    val enemyIds: List<String> = emptyList(),
    val treasure: Boolean = false,
    val boss: Boolean = false
)

@Serializable
data class LootTable(
    val id: String,
    val entries: List<LootEntry>
)

@Serializable
data class LootEntry(
    val itemId: String,
    val weight: Int,
    val minQuantity: Int = 1,
    val maxQuantity: Int = 1
)

@Serializable
data class EnemyDefinition(
    val id: String,
    val name: String,
    val level: Int,
    val hp: Int,
    val attack: Int,
    val defense: Int,
    val xp: Long,
    val lootTableId: String = "default"
)

@Serializable
data class ResourceNode(
    val id: String,
    val name: String,
    val biomeId: String,
    val resourceItemId: String,
    val quantity: Int = 1,
    val respawnSeconds: Int = 60
)
