package com.arkansasio.rpgmmo.network

import com.arkansasio.rpgmmo.model.Player
import kotlinx.serialization.Serializable

@Serializable data class LoginRequest(val email:String,val password:String)
@Serializable data class RegisterRequest(val email:String,val password:String,val displayName:String)
@Serializable data class RefreshRequest(val refreshToken:String)
@Serializable data class SessionResponse(val accessToken:String,val refreshToken:String,val expiresAtEpochSeconds:Long,val player:Player?=null)
@Serializable data class CharacterCreateRequest(val name:String,val classId:String,val raceId:String)
@Serializable data class CharacterResponse(val player:Player,val serverVersion:Long)
@Serializable data class SyncRequest(val clientRevision:Long,val operations:List<SyncOperation>)
@Serializable data class SyncOperation(val id:String,val type:String,val payload:String,val clientTimestampEpochMs:Long,val baseRevision:Long)
@Serializable data class SyncResponse(val acceptedOperationIds:List<String>,val rejectedOperationIds:List<String>,val player:Player?,val serverRevision:Long,val conflict:String?=null)
@Serializable data class MoveRequest(val characterId:String,val fromNodeId:String,val toNodeId:String,val clientRevision:Long)
@Serializable data class CombatRequest(val characterId:String,val targetId:String,val abilityId:String,val clientRevision:Long)
@Serializable data class CombatResponse(val accepted:Boolean,val combatId:String,val damage:Int,val critical:Boolean,val targetDefeated:Boolean,val serverRevision:Long,val errorCode:String?=null)
@Serializable data class GuildRequest(val guildId:String,val characterId:String,val operation:String,val targetPlayerId:String?=null,val name:String?=null)
@Serializable data class PartyRequest(val partyId:String,val characterId:String,val operation:String,val targetPlayerId:String?=null)
@Serializable data class MatchmakingRequest(val characterId:String,val queue:String,val rating:Int)
@Serializable data class MatchmakingResponse(val queued:Boolean,val matchId:String?=null,val playerIds:List<String>=emptyList(),val errorCode:String?=null)

object ServerEndpoints {
    const val REGISTER="/v1/auth/register"
    const val LOGIN="/v1/auth/login"
    const val REFRESH="/v1/auth/refresh"
    const val CHARACTER="/v1/characters"
    const val SYNC="/v1/sync"
    const val MOVE="/v1/world/move"
    const val COMBAT="/v1/combat/attack"
    const val GUILD="/v1/guilds/mutate"
    const val PARTY="/v1/parties/mutate"
    const val MATCHMAKING="/v1/matchmaking/queue"
}
