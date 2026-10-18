package com.arkansasio.rpgmmo.domain

import com.arkansasio.rpgmmo.model.Monster

object MonsterEngine {
    data class Result(val damage:Int,val critical:Boolean,val defeated:Boolean)
    fun attack(playerAttack:Int,monster:Monster,roll:Int):Result {
        val critical=roll.mod(100)<10
        val raw=(playerAttack-monster.defense).coerceAtLeast(1)*(if(critical)2 else 1)
        return Result(raw,critical,raw>=monster.hp)
    }
}

object GatheringEngine {
    fun gather(nodeId:String,nodes:Set<String>,amount:Int=1):Int = if(nodeId in nodes) amount.coerceAtLeast(1) else 0
}
