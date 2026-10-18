package com.arkansasio.rpgmmo

import com.arkansasio.rpgmmo.domain.CraftingEngine
import com.arkansasio.rpgmmo.domain.EquipmentEngine
import com.arkansasio.rpgmmo.domain.FactionEngine
import com.arkansasio.rpgmmo.domain.ProgressionEngine
import com.arkansasio.rpgmmo.model.*
import org.junit.Assert.*
import org.junit.Test

class MmoEngineTest {
    @Test fun progressionReachesExpectedLevel(){ assertEquals(2,ProgressionEngine.levelForXp(ProgressionEngine.requiredXp(2))) }
    @Test fun factionStandingChanges(){ val s=FactionEngine.modify(emptyList(),"f","Faction",100); assertEquals(100,FactionEngine.standing(s,"f")) }
    @Test fun equipmentReplacesSameSlot(){ val a=EquipmentEngine.equip(emptyList(),Equipment("weapon","a","A",5)); val b=EquipmentEngine.equip(a,Equipment("weapon","b","B",10)); assertEquals(1,b.size); assertEquals("b",b.single().itemId) }
    @Test fun craftingConsumesIngredients(){ val recipe=Recipe("r","Potion",listOf(RecipeIngredient("herb",2)),Item("potion","Potion",1)); val out=CraftingEngine.craft(listOf(Item("herb","Herb",2)),recipe)!!; assertEquals("potion",out.single().id) }
}
