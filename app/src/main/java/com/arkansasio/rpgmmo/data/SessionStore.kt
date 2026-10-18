package com.arkansasio.rpgmmo.data

import android.content.Context
import com.arkansasio.rpgmmo.network.SessionResponse

class SessionStore(context:Context) {
    private val prefs=context.getSharedPreferences("rpg_mmo_session",Context.MODE_PRIVATE)
    fun save(session:SessionResponse){prefs.edit().putString("access",session.accessToken).putString("refresh",session.refreshToken).putLong("expires",session.expiresAtEpochSeconds).apply()}
    fun accessToken():String?=prefs.getString("access",null)
    fun refreshToken():String?=prefs.getString("refresh",null)
    fun expiresAt():Long=prefs.getLong("expires",0L)
    fun isValid(nowEpochSeconds:Long=System.currentTimeMillis()/1000L):Boolean=!accessToken().isNullOrBlank() && expiresAt()>nowEpochSeconds+30
    fun clear(){prefs.edit().clear().apply()}
}
