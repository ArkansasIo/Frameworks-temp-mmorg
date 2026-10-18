package com.arkansasio.rpgmmo.server

import kotlinx.serialization.json.*
import java.util.UUID

object GameJson {
    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

    fun starter(name:String, id:String, classId:String, raceId:String): String = buildJsonObject {
        put("id",id); put("name",name); put("level",1); put("xp",0); put("gold",1000); put("hp",100)
        putJsonObject("stats") { put("str",10);put("dex",10);put("con",10);put("int",10);put("wis",10);put("vit",10);put("spi",10);put("lck",10);put("wil",10);put("cha",10) }
        putJsonArray("inventory") { add(buildJsonObject { put("id","starter-potion");put("name","Starter Potion");put("quantity",3);put("value",25) }) }
        putJsonArray("quests");putJsonArray("skills");putJsonArray("talents");putJsonArray("equipment");putJsonArray("factions")
        put("guild",JsonNull);put("party",JsonNull);put("pvpEnabled",false);put("currentNodeId","starter")
    }.toString()

    fun parse(value:String):JsonObject = json.parseToJsonElement(value).jsonObject
    fun encode(value:JsonObject):String = value.toString()
    fun withField(player:String,key:String,value:JsonElement):String = buildJsonObject {
        parse(player).forEach { (k,v) -> if(k != key) put(k,v) }
        put(key,value)
    }.toString()

    fun string(player:JsonObject,key:String,default:String="")=player[key]?.jsonPrimitive?.contentOrNull ?: default
    fun int(player:JsonObject,key:String,default:Int=0)=player[key]?.jsonPrimitive?.intOrNull ?: default
    fun long(player:JsonObject,key:String,default:Long=0)=player[key]?.jsonPrimitive?.longOrNull ?: default
    fun playerId(player:JsonObject)=string(player,"id")
    fun ensureId(player:String,id:String)=withField(player,"id",JsonPrimitive(id))
    fun newId()=UUID.randomUUID().toString()
}
