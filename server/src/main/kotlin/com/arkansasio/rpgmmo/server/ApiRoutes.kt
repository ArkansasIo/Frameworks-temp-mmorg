package com.arkansasio.rpgmmo.server

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import java.util.UUID
import kotlin.math.max

class ApiRoutes(private val app:Application, private val config:ServerConfig, private val db:Database) {
    private val json=Json{ignoreUnknownKeys=true;encodeDefaults=true}

    fun register() = with(app) { routing { route("/v1") {
        post("/auth/register") { register(call) }
        post("/auth/login") { login(call) }
        post("/auth/refresh") { refresh(call) }
        post("/characters") { authorized(call) { createCharacter(call,it) } }
        post("/sync") { authorized(call) { sync(call,it) } }
        post("/world/move") { authorized(call) { move(call,it) } }
        post("/combat/attack") { authorized(call) { combat(call,it) } }
        post("/guilds/mutate") { authorized(call) { guild(call,it) } }
        post("/parties/mutate") { authorized(call) { party(call,it) } }
        post("/matchmaking/queue") { authorized(call) { matchmaking(call,it) } }
        get("/health") { call.respond(HealthResponse("ok",System.currentTimeMillis())) }
    } } }

    private suspend fun register(call:ApplicationCall) {
        val req=call.receive<RegisterRequest>(); val email=req.email.trim().lowercase()
        if(!email.contains('@') || req.displayName.length !in 3..32) return call.respond(HttpStatusCode.BadRequest,ErrorResponse("INVALID_INPUT"))
        if(db.accountByEmail(email)!=null) return call.respond(HttpStatusCode.Conflict,ErrorResponse("ACCOUNT_EXISTS"))
        val account=db.createAccount(email,Passwords.hash(req.password))
        val player=db.createCharacter(account.id,req.displayName,"adventurer","human",GameJson.starter(req.displayName,"pending","adventurer","human"))
        val fixed=player.copy(json=GameJson.ensureId(player.json,player.id))
        db.transaction{c->c.prepareStatement("UPDATE characters SET player_json=? WHERE id=?").use{ps->ps.setString(1,fixed.json);ps.setString(2,player.id);ps.executeUpdate()}}
        call.respond(session(account.id,fixed.json))
    }

    private suspend fun login(call:ApplicationCall) {
        val req=call.receive<LoginRequest>(); val account=db.accountByEmail(req.email.trim().lowercase())
        if(account==null || !Passwords.verify(req.password,account.passwordHash)) return call.respond(HttpStatusCode.Unauthorized,ErrorResponse("INVALID_CREDENTIALS"))
        val player=db.playerForAccount(account.id); call.respond(session(account.id,player?.json))
    }

    private suspend fun refresh(call:ApplicationCall) {
        val req=call.receive<RefreshRequest>(); val new=Tokens.refresh()
        val accountId=db.rotateRefresh(Tokens.hashRefresh(req.refreshToken),Tokens.hashRefresh(new),now()+config.refreshTtlSeconds)
            ?: return call.respond(HttpStatusCode.Unauthorized,ErrorResponse("INVALID_REFRESH_TOKEN"))
        val player=db.playerForAccount(accountId)
        call.respond(SessionResponse(Tokens.access(accountId,config),new,now()+config.accessTtlSeconds,player?.json?.let(json::parseToJsonElement)))
    }

    private suspend fun createCharacter(call:ApplicationCall,accountId:String) {
        val req=call.receive<CharacterCreateRequest>(); if(req.name.length !in 3..32) return call.respond(HttpStatusCode.BadRequest,ErrorResponse("INVALID_NAME"))
        if(db.playerForAccount(accountId)!=null) return call.respond(HttpStatusCode.Conflict,ErrorResponse("CHARACTER_LIMIT"))
        val id=UUID.randomUUID().toString(); val player=db.createCharacter(accountId,req.name,req.classId,req.raceId,GameJson.starter(req.name,id,req.classId,req.raceId))
        call.respond(CharacterResponse(json.parseToJsonElement(player.json),player.revision))
    }

    private suspend fun sync(call:ApplicationCall,accountId:String) {
        val req=call.receive<SyncRequest>(); val current=db.playerForAccount(accountId) ?: return call.respond(HttpStatusCode.NotFound,ErrorResponse("CHARACTER_NOT_FOUND"))
        val accepted=mutableListOf<String>();val rejected=mutableListOf<String>();var conflict:String?=null
        db.transaction { c ->
            var record=current
            for(op in req.operations.distinctBy{it.id}) {
                if(db.hasSyncOperation(c,op.id)){accepted += op.id;continue}
                if(op.baseRevision != record.revision){rejected += op.id;conflict="STALE_REVISION";db.recordSync(c,op.id,record.id,op.baseRevision,false,conflict);continue}
                val payload=runCatching{json.parseToJsonElement(op.payload).jsonObject}.getOrNull()
                if(payload==null || payload["player"]==null){rejected += op.id;db.recordSync(c,op.id,record.id,op.baseRevision,false,"INVALID_PAYLOAD");continue}
                val p=payload["player"]!!.jsonObject
                val ok=db.updatePlayer(c,record.id,record.revision,GameJson.string(p,"name",record.name),GameJson.int(p,"level",record.level),GameJson.long(p,"xp",record.xp),GameJson.long(p,"gold",record.gold),GameJson.int(p,"hp",record.hp),GameJson.string(p,"currentNodeId",record.node),p.toString())
                if(ok){accepted += op.id;record=record.copy(name=GameJson.string(p,"name",record.name),level=GameJson.int(p,"level",record.level),xp=GameJson.long(p,"xp",record.xp),gold=GameJson.long(p,"gold",record.gold),hp=GameJson.int(p,"hp",record.hp),node=GameJson.string(p,"currentNodeId",record.node),revision=record.revision+1,json=p.toString());db.recordSync(c,op.id,record.id,op.baseRevision,true,null)} else {rejected += op.id;db.recordSync(c,op.id,record.id,op.baseRevision,false,"WRITE_CONFLICT")}
            }
        }
        val latest=db.playerForAccount(accountId) ?: current
        call.respond(SyncResponse(accepted,rejected,json.parseToJsonElement(latest.json),latest.revision,conflict));RealtimeHub.broadcast(accountId,"sync.completed")
    }

    private suspend fun move(call:ApplicationCall,accountId:String) {
        val req=call.receive<MoveRequest>(); val p=db.playerForAccount(accountId) ?: return call.respond(HttpStatusCode.NotFound,ErrorResponse("CHARACTER_NOT_FOUND"))
        if(req.characterId!=p.id || req.clientRevision!=p.revision) return call.respond(HttpStatusCode.Conflict,ErrorResponse("STALE_REVISION"))
        val valid=db.connection().use{c->c.prepareStatement("SELECT neighbors_json FROM world_nodes WHERE id=?").use{ps->ps.setString(1,req.fromNodeId);ps.executeQuery().use{if(it.next()) json.parseToJsonElement(it.getString(1)).jsonArray.any{n->n.jsonPrimitive.content==req.toNodeId}else false}}}
        if(p.node!=req.fromNodeId || !valid) return call.respond(HttpStatusCode.Conflict,ErrorResponse("INVALID_MOVE"))
        val updated=GameJson.withField(p.json,"currentNodeId",JsonPrimitive(req.toNodeId)); db.transaction{c->if(!db.updatePlayer(c,p.id,p.revision,p.name,p.level,p.xp,p.gold,p.hp,req.toNodeId,updated)) error("MOVE_CONFLICT")}
        call.respond(CharacterResponse(json.parseToJsonElement(updated),p.revision+1));RealtimeHub.broadcast(accountId,"world.moved")
    }

    private suspend fun combat(call:ApplicationCall,accountId:String) {
        val req=call.receive<CombatRequest>(); val p=db.playerForAccount(accountId) ?: return call.respond(HttpStatusCode.NotFound,ErrorResponse("CHARACTER_NOT_FOUND"))
        if(req.characterId!=p.id || req.clientRevision!=p.revision || p.hp<=0) return call.respond(HttpStatusCode.Conflict,ErrorResponse("STALE_OR_DEFEATED"))
        val seed=Math.floorMod((req.targetId+req.abilityId+p.revision).hashCode(),1000); val critical=seed>=900; val attack=max(1,10+p.level*2); val damage=if(critical)attack*2 else attack
        db.transaction{c->c.prepareStatement("INSERT INTO combat_events(id,character_id,target_id,ability_id,damage,critical,defeated) VALUES(?,?,?,?,?,?,?)").use{ps->ps.setString(1,UUID.randomUUID().toString());ps.setString(2,p.id);ps.setString(3,req.targetId);ps.setString(4,req.abilityId);ps.setInt(5,damage);ps.setBoolean(6,critical);ps.setBoolean(7,false);ps.executeUpdate()}}
        val response=CombatResponse(true,UUID.randomUUID().toString(),damage,critical,false,p.revision,null);call.respond(response);RealtimeHub.broadcast(accountId,"combat.hit")
    }

    private suspend fun guild(call:ApplicationCall,accountId:String) {
        val req=call.receive<GuildRequest>(); val p=db.playerForAccount(accountId) ?: return call.respond(HttpStatusCode.NotFound,ErrorResponse("CHARACTER_NOT_FOUND"))
        when(req.operation.lowercase()) {
            "create" -> { val name=req.name?.trim()?.takeIf{it.isNotBlank()} ?: return call.respond(HttpStatusCode.BadRequest,ErrorResponse("INVALID_GUILD_NAME")); val id=UUID.randomUUID().toString(); db.transaction{c->c.prepareStatement("INSERT INTO guilds(id,name,leader_id) VALUES(?,?,?)").use{ps->ps.setString(1,id);ps.setString(2,name);ps.setString(3,p.id);ps.executeUpdate()};c.prepareStatement("INSERT INTO guild_members(guild_id,character_id,officer) VALUES(?,?,TRUE)").use{ps->ps.setString(1,id);ps.setString(2,p.id);ps.executeUpdate()}};call.respond(GuildMutationResponse(true,id)) }
            "join" -> { val gid=req.guildId;db.transaction{c->c.prepareStatement("INSERT IGNORE INTO guild_members(guild_id,character_id) VALUES(?,?)").use{ps->ps.setString(1,gid);ps.setString(2,p.id);ps.executeUpdate()}};call.respond(GuildMutationResponse(true,gid)) }
            "leave" -> { db.transaction{c->c.prepareStatement("DELETE FROM guild_members WHERE guild_id=? AND character_id=?").use{ps->ps.setString(1,req.guildId);ps.setString(2,p.id);ps.executeUpdate()}};call.respond(GuildMutationResponse(true,req.guildId)) }
            else -> call.respond(HttpStatusCode.BadRequest,ErrorResponse("UNSUPPORTED_GUILD_OPERATION"))
        };RealtimeHub.broadcast(accountId,"guild.updated")
    }

    private suspend fun party(call:ApplicationCall,accountId:String) {
        val req=call.receive<PartyRequest>(); val p=db.playerForAccount(accountId) ?: return call.respond(HttpStatusCode.NotFound,ErrorResponse("CHARACTER_NOT_FOUND"))
        val id=if(req.partyId.isBlank())UUID.randomUUID().toString() else req.partyId
        db.transaction{c->if(req.operation.lowercase()=="create") { c.prepareStatement("INSERT INTO parties(id,leader_id) VALUES(?,?)").use{ps->ps.setString(1,id);ps.setString(2,p.id);ps.executeUpdate()} }; if(req.operation.lowercase() in setOf("create","join")) c.prepareStatement("INSERT IGNORE INTO party_members(party_id,character_id) VALUES(?,?)").use{ps->ps.setString(1,id);ps.setString(2,p.id);ps.executeUpdate()}; if(req.operation.lowercase()=="leave") c.prepareStatement("DELETE FROM party_members WHERE party_id=? AND character_id=?").use{ps->ps.setString(1,id);ps.setString(2,p.id);ps.executeUpdate()} }
        call.respond(PartyMutationResponse(true,id));RealtimeHub.broadcast(accountId,"party.updated")
    }

    private suspend fun matchmaking(call:ApplicationCall,accountId:String) {
        val req=call.receive<MatchmakingRequest>(); val p=db.playerForAccount(accountId) ?: return call.respond(HttpStatusCode.NotFound,ErrorResponse("CHARACTER_NOT_FOUND"));val ticket=UUID.randomUUID().toString()
        val match=db.transaction{c->c.prepareStatement("SELECT id,character_id FROM matchmaking_tickets WHERE queue_name=? AND ABS(rating-?)<=150 AND matched_at IS NULL AND character_id<>? ORDER BY created_at LIMIT 1 FOR UPDATE").use{ps->ps.setString(1,req.queue);ps.setInt(2,req.rating);ps.setString(3,p.id);ps.executeQuery().use{if(it.next())it.getString("id") to it.getString("character_id") else null}}}
        if(match!=null){db.transaction{c->c.prepareStatement("UPDATE matchmaking_tickets SET matched_at=UTC_TIMESTAMP() WHERE id=?").use{ps->ps.setString(1,match.first);ps.executeUpdate()};c.prepareStatement("INSERT INTO matchmaking_tickets(id,character_id,queue_name,rating,matched_at) VALUES(?,?,?,?,UTC_TIMESTAMP())").use{ps->ps.setString(1,ticket);ps.setString(2,p.id);ps.setString(3,req.queue);ps.setInt(4,req.rating);ps.executeUpdate()}};val mid=UUID.randomUUID().toString();call.respond(MatchmakingResponse(true,mid,listOf(p.id,match.second),null));RealtimeHub.broadcast(accountId,"match.found")} else {db.transaction{c->c.prepareStatement("INSERT INTO matchmaking_tickets(id,character_id,queue_name,rating) VALUES(?,?,?,?)").use{ps->ps.setString(1,ticket);ps.setString(2,p.id);ps.setString(3,req.queue);ps.setInt(4,req.rating);ps.executeUpdate()}};call.respond(MatchmakingResponse(true,null,emptyList(),null))}
    }

    private suspend fun authorized(call:ApplicationCall,block:suspend(ApplicationCall,String)->Unit){val header=call.request.headers[HttpHeaders.Authorization] ?: return call.respond(HttpStatusCode.Unauthorized,ErrorResponse("MISSING_AUTH"));val token=header.removePrefix("Bearer ").trim();val user=Tokens.verifyAccess(token,config) ?: return call.respond(HttpStatusCode.Unauthorized,ErrorResponse("INVALID_AUTH"));block(call,user)}
    private fun session(accountId:String,playerJson:String?):SessionResponse{val refresh=Tokens.refresh();db.createSession(accountId,Tokens.hashRefresh(refresh),now()+config.refreshTtlSeconds);return SessionResponse(Tokens.access(accountId,config),refresh,now()+config.accessTtlSeconds,playerJson?.let(json::parseToJsonElement))}
    private fun now()=System.currentTimeMillis()/1000
}

@Serializable data class RegisterRequest(val email:String,val password:String,val displayName:String)
@Serializable data class LoginRequest(val email:String,val password:String)
@Serializable data class RefreshRequest(val refreshToken:String)
@Serializable data class SessionResponse(val accessToken:String,val refreshToken:String,val expiresAtEpochSeconds:Long,val player:JsonElement?=null)
@Serializable data class CharacterCreateRequest(val name:String,val classId:String,val raceId:String)
@Serializable data class CharacterResponse(val player:JsonElement,val serverVersion:Long)
@Serializable data class SyncRequest(val clientRevision:Long,val operations:List<SyncOperationDto>)
@Serializable data class SyncOperationDto(val id:String,val type:String,val payload:String,val clientTimestampEpochMs:Long,val baseRevision:Long)
@Serializable data class SyncResponse(val acceptedOperationIds:List<String>,val rejectedOperationIds:List<String>,val player:JsonElement?,val serverRevision:Long,val conflict:String?=null)
@Serializable data class MoveRequest(val characterId:String,val fromNodeId:String,val toNodeId:String,val clientRevision:Long)
@Serializable data class CombatRequest(val characterId:String,val targetId:String,val abilityId:String,val clientRevision:Long)
@Serializable data class CombatResponse(val accepted:Boolean,val combatId:String,val damage:Int,val critical:Boolean,val targetDefeated:Boolean,val serverRevision:Long,val errorCode:String?=null)
@Serializable data class GuildRequest(val guildId:String,val characterId:String,val operation:String,val targetPlayerId:String?=null,val name:String?=null)
@Serializable data class PartyRequest(val partyId:String,val characterId:String,val operation:String,val targetPlayerId:String?=null)
@Serializable data class MatchmakingRequest(val characterId:String,val queue:String,val rating:Int)
@Serializable data class MatchmakingResponse(val queued:Boolean,val matchId:String?=null,val playerIds:List<String>=emptyList(),val errorCode:String?=null)
@Serializable data class GuildMutationResponse(val accepted:Boolean,val guildId:String)
@Serializable data class PartyMutationResponse(val accepted:Boolean,val partyId:String)
@Serializable data class HealthResponse(val status:String,val timestampEpochMs:Long)
