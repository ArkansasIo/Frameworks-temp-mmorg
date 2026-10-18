package com.arkansasio.rpgmmo.domain

import com.arkansasio.rpgmmo.model.Stats
import kotlin.math.max
import kotlin.math.pow

object ProgressionEngine {
    fun requiredXp(level:Int):Long = if(level<=1) 0L else (level.toDouble().pow(2.5)*500.0).toLong()
    fun levelForXp(xp:Long, cap:Int=50):Int { var level=1; while(level<cap && xp>=requiredXp(level+1)) level++; return level }
    fun maxHp(stats:Stats):Int = stats.maxHp
}

object CombatEngine {
    data class Result(val damage:Int,val critical:Boolean,val defeated:Boolean)
    fun attack(attacker:Stats, defender:Stats, seed:Double=0.5):Result {
        val critical = seed >= 0.9
        val base = max(1, attacker.attackPower - defender.defense/2)
        val damage = if(critical) base*2 else base
        return Result(damage, critical, false)
    }
    fun mitigatedDamage(raw:Int, armor:Int, constant:Double=100.0):Int = max(0,(raw*constant/(constant+max(0,armor))).toInt())
}
