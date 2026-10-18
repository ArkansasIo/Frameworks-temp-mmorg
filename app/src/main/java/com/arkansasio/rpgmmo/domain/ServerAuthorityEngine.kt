package com.arkansasio.rpgmmo.domain

import com.arkansasio.rpgmmo.model.Player
import com.arkansasio.rpgmmo.model.WorldNode

object ServerAuthorityEngine {
    data class Validation(val accepted:Boolean,val reason:String?=null)
    fun validateMove(player:Player,fromNodeId:String,toNodeId:String,nodes:List<WorldNode>):Validation {
        if(player.currentNodeId!=fromNodeId) return Validation(false,"STALE_POSITION")
        if(!WorldEngine.canMove(nodes,fromNodeId,toNodeId)) return Validation(false,"INVALID_EDGE")
        return Validation(true)
    }
    fun validateCombat(player:Player,targetId:String,abilityId:String):Validation {
        if(player.hp<=0) return Validation(false,"PLAYER_DEFEATED")
        if(targetId.isBlank() || abilityId.isBlank()) return Validation(false,"INVALID_REQUEST")
        return Validation(true)
    }
    fun validateInventoryDelta(player:Player,itemId:String,delta:Int):Validation {
        if(itemId.isBlank() || delta==0) return Validation(false,"INVALID_DELTA")
        if(delta<0 && (player.inventory.firstOrNull{it.id==itemId}?.quantity ?: 0) < -delta) return Validation(false,"INSUFFICIENT_ITEM")
        return Validation(true)
    }
    fun validateGoldDelta(player:Player,delta:Long):Validation = if(delta<0 && player.gold < -delta) Validation(false,"INSUFFICIENT_GOLD") else Validation(true)
}
