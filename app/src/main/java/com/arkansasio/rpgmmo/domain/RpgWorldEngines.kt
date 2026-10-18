package com.arkansasio.rpgmmo.domain

import com.arkansasio.rpgmmo.model.*
import kotlin.random.Random

object ZoneEngine {
    fun zoneFor(player: Player, zones: List<Zone>): Zone? = zones.firstOrNull { it.nodeIds.contains(player.currentNodeId) }
    fun canEnter(player: Player, zone: Zone): Boolean = player.level >= zone.recommendedLevel
    fun biomeFor(zone: Zone, biomes: List<Biome>): Biome? = biomes.firstOrNull { it.id == zone.biomeId }
}

object MapEngine {
    fun reachableNodes(start: String, nodes: List<WorldNode>): Set<String> {
        val byId = nodes.associateBy { it.id }
        val seen = mutableSetOf<String>()
        val queue = ArrayDeque<String>()
        queue.add(start)
        while (queue.isNotEmpty()) {
            val id = queue.removeFirst()
            if (!seen.add(id)) continue
            byId[id]?.neighbors?.forEach { if (!seen.contains(it)) queue.add(it) }
        }
        return seen
    }

    fun pointsFor(zoneId: String, map: WorldMap): List<MapPoint> = map.points.filter { it.zoneId == zoneId }
}

object DungeonEngine {
    fun generateRooms(dungeon: Dungeon, enemies: List<EnemyDefinition>): List<DungeonRoom> {
        val normal = enemies.filter { it.level in dungeon.levelMin..dungeon.levelMax && it.id != dungeon.bossId }
        return (0 until dungeon.rooms).map { index ->
            val boss = index == dungeon.rooms - 1
            val selected = if (boss) listOf(dungeon.bossId) else normal.take(2).map { it.id }
            DungeonRoom(
                id = "${dungeon.id}-room-$index",
                dungeonId = dungeon.id,
                index = index,
                name = if (boss) "${dungeon.name} - Boss Chamber" else "Room ${index + 1}",
                enemyIds = selected,
                treasure = !boss && index % 2 == 1,
                boss = boss
            )
        }
    }

    fun recommendedPower(dungeon: Dungeon): Int = dungeon.levelMin * 20 + dungeon.recommendedPlayers * 10
}

object LootEngine {
    fun roll(table: LootTable, random: Random = Random.Default): LootEntry? {
        if (table.entries.isEmpty()) return null
        val total = table.entries.sumOf { it.weight.coerceAtLeast(0) }
        if (total <= 0) return table.entries.first()
        var roll = random.nextInt(total)
        for (entry in table.entries) {
            roll -= entry.weight.coerceAtLeast(0)
            if (roll < 0) return entry
        }
        return table.entries.last()
    }

    fun quantity(entry: LootEntry, random: Random = Random.Default): Int =
        random.nextInt(entry.minQuantity, entry.maxQuantity.coerceAtLeast(entry.minQuantity) + 1)
}

object InventoryBagEngine {
    fun add(bag: InventoryBag, itemId: String, quantity: Int, definitions: Map<String, ItemDefinition>): InventoryBag {
        if (quantity <= 0) return bag
        val definition = definitions[itemId] ?: return bag
        val mutable = bag.slots.toMutableList()
        var remaining = quantity
        mutable.indices.filter { mutable[it].itemId == itemId && mutable[it].quantity < definition.stackSize }.forEach { index ->
            if (remaining <= 0) return@forEach
            val room = definition.stackSize - mutable[index].quantity
            val amount = minOf(room, remaining)
            mutable[index] = mutable[index].copy(quantity = mutable[index].quantity + amount)
            remaining -= amount
        }
        mutable.indices.filter { mutable[it].itemId == null && !mutable[it].locked }.forEach { index ->
            if (remaining <= 0) return@forEach
            val amount = minOf(definition.stackSize, remaining)
            mutable[index] = mutable[index].copy(itemId = itemId, quantity = amount)
            remaining -= amount
        }
        return bag.copy(slots = mutable)
    }

    fun remove(bag: InventoryBag, itemId: String, quantity: Int): InventoryBag? {
        if (quantity <= 0) return bag
        if (bag.slots.filter { it.itemId == itemId }.sumOf { it.quantity } < quantity) return null
        val mutable = bag.slots.toMutableList()
        var remaining = quantity
        mutable.indices.reversed().filter { mutable[it].itemId == itemId }.forEach { index ->
            if (remaining <= 0) return@forEach
            val amount = minOf(mutable[index].quantity, remaining)
            val next = mutable[index].quantity - amount
            mutable[index] = if (next == 0) mutable[index].copy(itemId = null, quantity = 0) else mutable[index].copy(quantity = next)
            remaining -= amount
        }
        return bag.copy(slots = mutable)
    }

    fun count(bag: InventoryBag, itemId: String): Int = bag.slots.filter { it.itemId == itemId }.sumOf { it.quantity }
}

object EquipmentLoadoutEngine {
    fun equip(loadout: EquipmentLoadout, definition: ItemDefinition): EquipmentLoadout {
        val slot = definition.equipmentSlot ?: return loadout
        return loadout.copy(slots = loadout.slots + (slot to definition.id))
    }

    fun unequip(loadout: EquipmentLoadout, slot: EquipmentSlot): EquipmentLoadout =
        loadout.copy(slots = loadout.slots - slot)

    fun totalPower(loadout: EquipmentLoadout, definitions: Map<String, ItemDefinition>): Int =
        loadout.slots.values.sumOf { definitions[it]?.power ?: 0 }
}
