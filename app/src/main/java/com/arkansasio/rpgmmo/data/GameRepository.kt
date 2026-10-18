package com.arkansasio.rpgmmo.data

import com.arkansasio.rpgmmo.model.*

class GameRepository(private val store:LocalStore) {
    fun load():Player=store.load()
    fun save(player:Player)=store.save(player)
    fun addItem(player:Player,item:Item)=player.copy(inventory=player.inventory+item).also(store::save)
    fun togglePvp(player:Player):Player=player.copy(pvpEnabled=!player.pvpEnabled).also(store::save)
    fun grantXp(player:Player,xp:Long):Player=player.copy(xp=(player.xp+xp).coerceAtLeast(0)).also(store::save)
}
