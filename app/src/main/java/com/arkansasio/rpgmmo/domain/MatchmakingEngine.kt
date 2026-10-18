package com.arkansasio.rpgmmo.domain

import com.arkansasio.rpgmmo.model.MatchResult
import com.arkansasio.rpgmmo.model.MatchTicket
import java.util.UUID

object MatchmakingEngine {
    private val queues=mutableMapOf<String,MutableList<MatchTicket>>()
    fun enqueue(ticket:MatchTicket):MatchResult? { val q=queues.getOrPut(ticket.queue){mutableListOf()}; if(q.none{it.playerId==ticket.playerId}) q+=ticket; if(q.size<2) return null; val a=q.removeAt(0); val b=q.removeAt(0); return MatchResult(UUID.randomUUID().toString(),listOf(a.playerId,b.playerId)) }
    fun leave(queue:String,playerId:String){ queues[queue]?.removeAll{it.playerId==playerId} }
}
