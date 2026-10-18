package com.arkansasio.rpgmmo.data

import android.content.Context
import com.arkansasio.rpgmmo.model.Player
import kotlinx.serialization.json.Json

class LocalStore(context:Context) {
    private val prefs=context.getSharedPreferences("rpg_mmo",Context.MODE_PRIVATE)
    private val json=Json{ignoreUnknownKeys=true}
    fun load():Player = prefs.getString("player",null)?.let{runCatching{json.decodeFromString<Player>(it)}.getOrNull()} ?: Player()
    fun save(player:Player){ prefs.edit().putString("player",json.encodeToString(player)).apply() }
    fun clear(){ prefs.edit().clear().apply() }
}
