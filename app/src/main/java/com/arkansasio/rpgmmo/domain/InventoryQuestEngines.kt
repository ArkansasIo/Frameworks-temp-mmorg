package com.arkansasio.rpgmmo.domain

import com.arkansasio.rpgmmo.model.Item

object InventoryEngine {
    fun add(items:List<Item>, item:Item):List<Item> {
        val existing=items.indexOfFirst{it.id==item.id}; if(existing<0) return items+item
        return items.toMutableList().also{it[existing]=it[existing].copy(quantity=it[existing].quantity+item.quantity)}
    }
    fun remove(items:List<Item>, id:String, quantity:Int):List<Item>? {
        if(quantity<=0) return items
        val index=items.indexOfFirst{it.id==id}; if(index<0 || items[index].quantity<quantity) return null
        val out=items.toMutableList(); val next=out[index].quantity-quantity
        if(next==0) out.removeAt(index) else out[index]=out[index].copy(quantity=next)
        return out
    }
}

object QuestEngine {
    fun advance(quests:List<com.arkansasio.rpgmmo.model.Quest>, id:String, amount:Int):List<com.arkansasio.rpgmmo.model.Quest> = quests.map { q ->
        if(q.id!=id || q.completed) q else { val p=(q.progress+amount).coerceAtMost(q.required); q.copy(progress=p,completed=p>=q.required) }
    }
}
