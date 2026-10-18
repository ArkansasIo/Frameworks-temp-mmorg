package com.arkansasio.rpgmmo.model

import kotlinx.serialization.Serializable

@Serializable data class Stats(val str:Int=10,val dex:Int=10,val con:Int=10,val int:Int=10,val wis:Int=10,val vit:Int=10,val spi:Int=10,val lck:Int=10,val wil:Int=10,val cha:Int=10) {
    val maxHp:Int get() = vit * 15 + con * 10
    val attackPower:Int get() = str * 2 + dex
    val defense:Int get() = con * 2 + vit
}
@Serializable data class Item(val id:String,val name:String,val quantity:Int=1,val value:Long=0)
@Serializable data class Quest(val id:String,val name:String,val progress:Int=0,val required:Int=1,val completed:Boolean=false)
@Serializable data class Skill(val id:String,val name:String,val rank:Int=1,val maxRank:Int=10)
@Serializable data class Talent(val id:String,val name:String,val rank:Int=0,val maxRank:Int=5)
@Serializable data class Player(val id:String="local",val name:String="Commander",val level:Int=1,val xp:Long=0,val stats:Stats=Stats(),val hp:Int=100,val gold:Long=1000,val inventory:List<Item>=emptyList(),val quests:List<Quest>=emptyList(),val skills:List<Skill>=emptyList(),val talents:List<Talent>=emptyList(),val pvpEnabled:Boolean=false)
@Serializable data class MatchTicket(val queue:String,val playerId:String,val rating:Int=1000)
@Serializable data class MatchResult(val matchId:String,val players:List<String>)
