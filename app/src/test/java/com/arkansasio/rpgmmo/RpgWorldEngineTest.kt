package com.arkansasio.rpgmmo

import com.arkansasio.rpgmmo.domain.DungeonEngine
import com.arkansasio.rpgmmo.domain.InventoryBagEngine
import com.arkansasio.rpgmmo.domain.LootEngine
import com.arkansasio.rpgmmo.model.*
import org.junit.Assert.*
import org.junit.Test

class RpgWorldEngineTest {
    private val sword = ItemDefinition("sword", "Sword", "", ItemRarity.COMMON, stackSize = 1, equipmentSlot = EquipmentSlot.MAIN_HAND, power = 10)

    @Test fun inventoryStacksAndRemoves() {
        val defs = mapOf(sword.id to sword)
        val bag = InventoryBag(capacity = 4, slots = (0 until 4).map { InventorySlot(it) })
        val filled = InventoryBagEngine.add(bag, "sword", 1, defs)
        assertEquals(1, InventoryBagEngine.count(filled, "sword"))
        val removed = InventoryBagEngine.remove(filled, "sword", 1)
        assertNotNull(removed)
        assertEquals(0, InventoryBagEngine.count(removed!!, "sword"))
    }

    @Test fun dungeonGeneratesBossRoom() {
        val dungeon = Dungeon("d", "Test Dungeon", "z", 1, 10, 3, "boss")
        val enemies = listOf(EnemyDefinition("rat", "Rat", 1, 10, 2, 1, 10), EnemyDefinition("boss", "Boss", 10, 100, 20, 10, 100))
        val rooms = DungeonEngine.generateRooms(dungeon, enemies)
        assertEquals(3, rooms.size)
        assertTrue(rooms.last().boss)
        assertEquals("boss", rooms.last().enemyIds.single())
    }

    @Test fun lootRollReturnsEntry() {
        val table = LootTable("t", listOf(LootEntry("sword", 1)))
        assertEquals("sword", LootEngine.roll(table, kotlin.random.Random(1))?.itemId)
    }
}
