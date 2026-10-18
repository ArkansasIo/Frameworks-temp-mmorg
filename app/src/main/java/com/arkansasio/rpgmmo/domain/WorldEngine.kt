package com.arkansasio.rpgmmo.domain

import com.arkansasio.rpgmmo.model.*

object WorldEngine {
    fun canWalk(map:WorldMap,x:Int,y:Int):Boolean = map.tiles.firstOrNull{it.x==x&&it.y==y}?.walkable==true
    fun neighbors(map:WorldMap,x:Int,y:Int):List<Tile> = listOf(x+1 to y,x-1 to y,x to y+1,x to y-1).mapNotNull{(tx,ty)->map.tiles.firstOrNull{it.x==tx&&it.y==ty&&it.walkable}}
    fun zoneFor(zones:List<Zone>,level:Int):List<Zone> = zones.filter{level in it.minLevel..it.maxLevel}
    fun reachable(map:WorldMap,startX:Int,startY:Int,targetX:Int,targetY:Int):Boolean {
        if(!canWalk(map,startX,startY)||!canWalk(map,targetX,targetY)) return false
        val seen=mutableSetOf(startX to startY); val queue=ArrayDeque<Pair<Int,Int>>(); queue.add(startX to startY)
        while(queue.isNotEmpty()) { val p=queue.removeFirst(); if(p.first==targetX&&p.second==targetY)return true; neighbors(map,p.first,p.second).forEach{n->val q=n.x to n.y;if(seen.add(q))queue.add(q)} }
        return false
    }
}

object DungeonEngine {
    fun createRun(dungeon:Dungeon):List<String> = buildList { repeat(dungeon.rooms.coerceAtLeast(1)){i->add(if(i==dungeon.rooms-1)dungeon.bossId else "${dungeon.id}-room-${i+1}")} }
}

object LootEngine {
    fun roll(table:LootTable,roll:Int):LootEntry? {
        val total=table.entries.sumOf{it.weight}.coerceAtLeast(1); var cursor=roll.mod(total)
        table.entries.forEach{if(cursor<it.weight)return it;cursor-=it.weight}; return table.entries.lastOrNull()
    }
}

object InventoryEngineV2 {
    fun add(inv:Inventory,itemId:String,quantity:Int):Inventory {
        if(quantity<=0)return inv
        val slots=inv.slots.toMutableList(); val existing=slots.indexOfFirst{it.itemId==itemId}
        if(existing>=0){slots[existing]=slots[existing].copy(quantity=slots[existing].quantity+quantity);return inv.copy(slots=slots)}
        if(slots.size>=inv.capacity)return inv
        slots.add(InventorySlot(slots.size,itemId,quantity));return inv.copy(slots=slots)
    }
    fun remove(inv:Inventory,itemId:String,quantity:Int):Inventory {
        if(quantity<=0)return inv
        val i=inv.slots.indexOfFirst{it.itemId==itemId}; if(i<0)return inv
        val slots=inv.slots.toMutableList(); val s=slots[i]; val left=s.quantity-quantity
        if(left>0)slots[i]=s.copy(quantity=left) else slots.removeAt(i)
        return inv.copy(slots=slots.mapIndexed{n,v->v.copy(slot=n)})
    }
}
