package com.arkansasio.rpgmmo.domain

import com.arkansasio.rpgmmo.model.*

object FactionEngine {
    fun set(standings:List<FactionStanding>, id:String, name:String, value:Int):List<FactionStanding> = standings.filterNot { it.factionId == id } + FactionStanding(id,name,value.coerceIn(-10000,10000))
    fun modify(standings:List<FactionStanding>, id:String, name:String, delta:Int):List<FactionStanding> = set(standings,id,name,(standings.firstOrNull{it.factionId==id}?.standing ?: 0)+delta)
    fun standing(standings:List<FactionStanding>,id:String):Int = standings.firstOrNull{it.factionId==id}?.standing ?: 0
    fun hostile(standings:List<FactionStanding>,id:String,threshold:Int=-1000):Boolean = standing(standings,id)<=threshold
}

object GuildEngine {
    fun create(playerId:String,playerName:String,id:String,name:String):Guild = Guild(id,name,playerId,listOf(GuildMember(playerId,playerName,true)))
    fun addMember(guild:Guild,member:GuildMember):Guild = if(guild.members.any{it.playerId==member.playerId}) guild else guild.copy(members=guild.members+member)
    fun removeMember(guild:Guild,playerId:String):Guild = guild.copy(members=guild.members.filterNot{it.playerId==playerId})
}

object PartyEngine {
    fun create(id:String,leader:PartyMember):Party=Party(id,listOf(leader.copy(leader=true)))
    fun add(party:Party,member:PartyMember):Party=if(party.members.any{it.playerId==member.playerId})party else party.copy(members=party.members+member)
    fun remove(party:Party,playerId:String):Party=party.copy(members=party.members.filterNot{it.playerId==playerId})
}

object SkillEngine {
    fun learn(skills:List<Skill>,skill:Skill):List<Skill>{
        if(skills.none{it.id==skill.id}) return skills+skill.copy(rank=skill.rank.coerceIn(1,skill.maxRank))
        return skills.map{if(it.id==skill.id)it.copy(rank=(it.rank+skill.rank).coerceAtMost(it.maxRank))else it}
    }
    fun train(skills:List<Skill>,id:String):List<Skill>=skills.map{if(it.id==id)it.copy(rank=(it.rank+1).coerceAtMost(it.maxRank))else it}
}

object TalentEngine {
    fun invest(talents:List<Talent>,id:String):List<Talent>=talents.map{if(it.id==id)it.copy(rank=(it.rank+1).coerceAtMost(it.maxRank))else it}
}

object EquipmentEngine {
    fun equip(equipment:List<Equipment>,item:Equipment):List<Equipment>=equipment.filterNot{it.slot==item.slot}+item
    fun totalPower(equipment:List<Equipment>):Int=equipment.sumOf{it.power}
}

object CraftingEngine {
    fun canCraft(inventory:List<Item>,recipe:Recipe):Boolean=recipe.ingredients.all{ingredient -> (inventory.firstOrNull{it.id==ingredient.itemId}?.quantity ?: 0) >= ingredient.quantity}
    fun craft(inventory:List<Item>,recipe:Recipe):List<Item>? {
        if(!canCraft(inventory,recipe))return null
        var result=inventory
        for(ingredient in recipe.ingredients){ result=InventoryEngine.remove(result,ingredient.itemId,ingredient.quantity) ?: return null }
        return InventoryEngine.add(result,recipe.output)
    }
}

object ShopEngine {
    fun buy(gold:Long,inventory:List<Item>,entry:ShopEntry,quantity:Int):Pair<Long,List<Item>>? {
        if(quantity<=0 || quantity>entry.stock)return null
        val cost=entry.buyPrice*quantity
        if(gold<cost)return null
        return (gold-cost) to InventoryEngine.add(inventory,entry.item.copy(quantity=quantity))
    }
    fun sell(gold:Long,inventory:List<Item>,entry:ShopEntry,quantity:Int):Pair<Long,List<Item>>? {
        if(quantity<=0)return null
        val updated=InventoryEngine.remove(inventory,entry.item.id,quantity) ?: return null
        return (gold+entry.sellPrice*quantity) to updated
    }
}

object WorldEngine {
    fun canMove(nodes:List<WorldNode>,from:String,to:String):Boolean=nodes.firstOrNull{it.id==from}?.neighbors?.contains(to)==true
    fun move(player:Player,nodes:List<WorldNode>,to:String):Player=if(canMove(nodes,player.currentNodeId,to))player.copy(currentNodeId=to)else player
}
