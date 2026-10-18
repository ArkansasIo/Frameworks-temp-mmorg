package com.arkansasio.rpgmmo.model

import kotlinx.serialization.Serializable

@Serializable data class Stats(val str:Int=10,val dex:Int=10,val con:Int=10,val int:Int=10,val wis:Int=10,val vit:Int=10,val spi:Int=10,val lck:Int=10,val wil:Int=10,val cha:Int=10) {
    val maxHp:Int get() = vit * 15 + con * 10
    val attackPower:Int get() = str * 2 + dex
    val defense:Int get() = con * 2 + vit
    val magicPower:Int get() = int * 2 + wis
    val critChance:Double get() = (lck * 0.5).coerceIn(0.0,50.0)
}
@Serializable data class Item(val id:String,val name:String,val quantity:Int=1,val value:Long=0)
@Serializable data class Quest(val id:String,val name:String,val progress:Int=0,val required:Int=1,val completed:Boolean=false)
@Serializable data class Skill(val id:String,val name:String,val rank:Int=1,val maxRank:Int=10)
@Serializable data class Talent(val id:String,val name:String,val rank:Int=0,val maxRank:Int=5)
@Serializable data class Equipment(val slot:String,val itemId:String,val itemName:String,val power:Int=0)
@Serializable data class FactionStanding(val factionId:String,val name:String,val standing:Int=0)
@Serializable data class GuildMember(val playerId:String,val name:String,val officer:Boolean=false)
@Serializable data class Guild(val id:String,val name:String,val leaderId:String,val members:List<GuildMember>=emptyList())
@Serializable data class PartyMember(val playerId:String,val name:String,val leader:Boolean=false)
@Serializable data class Party(val id:String,val members:List<PartyMember>=emptyList())
@Serializable data class RecipeIngredient(val itemId:String,val quantity:Int)
@Serializable data class Recipe(val id:String,val name:String,val ingredients:List<RecipeIngredient>,val output:Item)
@Serializable data class ShopEntry(val item:Item,val buyPrice:Long,val sellPrice:Long,val stock:Int)
@Serializable data class WorldNode(val id:String,val name:String,val zone:String,val neighbors:List<String>=emptyList())
@Serializable data class Npc(val id:String,val name:String,val level:Int=1,val hostile:Boolean=false)
@Serializable data class MatchTicket(val queue:String,val playerId:String,val rating:Int=1000)
@Serializable data class MatchResult(val matchId:String,val players:List<String>)
@Serializable data class Player(val id:String="local",val name:String="Commander",val level:Int=1,val xp:Long=0,val stats:Stats=Stats(),val hp:Int=100,val gold:Long=1000,val inventory:List<Item>=emptyList(),val quests:List<Quest>=emptyList(),val skills:List<Skill>=emptyList(),val talents:List<Talent>=emptyList(),val equipment:List<Equipment>=emptyList(),val factions:List<FactionStanding>=emptyList(),val guild:Guild?=null,val party:Party?=null,val pvpEnabled:Boolean=false,val currentNodeId:String="starter")
