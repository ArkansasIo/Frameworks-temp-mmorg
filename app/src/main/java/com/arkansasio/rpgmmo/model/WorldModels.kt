package com.arkansasio.rpgmmo.model

import kotlinx.serialization.Serializable

@Serializable
data class Tile(val x:Int,val y:Int,val terrain:String,val walkable:Boolean=true,val resource:String?=null)

@Serializable
data class WorldMap(val id:String,val name:String,val width:Int,val height:Int,val tiles:List<Tile>)

@Serializable
data class Zone(val id:String,val name:String,val biome:String,val minLevel:Int,val maxLevel:Int,val mapId:String,val safe:Boolean=false,val dungeonIds:List<String> = emptyList())

@Serializable
data class Dungeon(val id:String,val name:String,val zoneId:String,val minLevel:Int,val maxLevel:Int,val partySize:Int,val rooms:Int,val bossId:String,val lootTableId:String)

@Serializable
data class Monster(val id:String,val name:String,val level:Int,val hp:Int,val attack:Int,val defense:Int,val xp:Int,val lootTableId:String)

@Serializable
data class LootEntry(val itemId:String,val weight:Int,val minQuantity:Int=1,val maxQuantity:Int=1)

@Serializable
data class LootTable(val id:String,val entries:List<LootEntry>)

@Serializable
enum class EquipmentSlot { HEAD,CHEST,LEGS,FEET,HANDS,MAIN_HAND,OFF_HAND,RING,AMULET,BACK }

@Serializable
enum class ItemRarity { COMMON,UNCOMMON,RARE,EPIC,LEGENDARY,MYTHIC }

@Serializable
data class GearItem(val id:String,val name:String,val slot:EquipmentSlot,val rarity:ItemRarity,val requiredLevel:Int,val power:Int,val statBonuses:Map<String,Int> = emptyMap(),val setId:String?=null)

@Serializable
data class InventorySlot(val slot:Int,val itemId:String?=null,val quantity:Int=0)

@Serializable
data class Inventory(val capacity:Int=40,val slots:List<InventorySlot> = emptyList())

@Serializable
data class MapMarker(val id:String,val name:String,val x:Int,val y:Int,val type:String)

@Serializable
data class GatheringNode(val id:String,val name:String,val resourceItemId:String,val respawnSeconds:Int,val x:Int,val y:Int)
