package com.arkansasio.rpgmmo.model

import kotlinx.serialization.Serializable

@Serializable
enum class EquipmentSlot { HEAD, CHEST, LEGS, FEET, HANDS, MAIN_HAND, OFF_HAND, RING, AMULET, BACK }

@Serializable
enum class ItemRarity { COMMON, UNCOMMON, RARE, EPIC, LEGENDARY, MYTHIC }

@Serializable
data class ItemDefinition(
    val id: String,
    val name: String,
    val description: String,
    val rarity: ItemRarity = ItemRarity.COMMON,
    val levelRequired: Int = 1,
    val stackSize: Int = 99,
    val baseValue: Long = 0,
    val equipmentSlot: EquipmentSlot? = null,
    val power: Int = 0,
    val statBonuses: Map<String, Int> = emptyMap(),
    val icon: String = "item_unknown"
)

@Serializable
data class InventorySlot(
    val index: Int,
    val itemId: String? = null,
    val quantity: Int = 0,
    val locked: Boolean = false
)

@Serializable
data class InventoryBag(
    val capacity: Int = 40,
    val slots: List<InventorySlot> = (0 until 40).map { InventorySlot(it) }
)

@Serializable
data class EquipmentLoadout(
    val slots: Map<EquipmentSlot, String> = emptyMap()
)

@Serializable
data class GearSet(
    val id: String,
    val name: String,
    val itemIds: List<String>,
    val setBonusDescription: String,
    val setBonusPower: Int = 0
)

@Serializable
data class CraftingRecipe(
    val id: String,
    val name: String,
    val outputItemId: String,
    val outputQuantity: Int = 1,
    val ingredients: Map<String, Int>,
    val requiredLevel: Int = 1,
    val station: String = "forge"
)

@Serializable
data class Achievement(
    val id: String,
    val name: String,
    val description: String,
    val progress: Int = 0,
    val target: Int = 1,
    val rewardGold: Long = 0,
    val rewardXp: Long = 0,
    val completed: Boolean = false
)
