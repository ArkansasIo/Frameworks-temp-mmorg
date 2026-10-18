package com.arkansasio.rpgmmo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.arkansasio.rpgmmo.data.GameRepository
import com.arkansasio.rpgmmo.data.LocalStore
import com.arkansasio.rpgmmo.domain.*
import com.arkansasio.rpgmmo.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GameViewModel(app:Application):AndroidViewModel(app) {
    private val repo=GameRepository(LocalStore(app))
    private val _player=MutableStateFlow(repo.load())
    val player:StateFlow<Player> = _player
    private fun update(p:Player){_player.value=p;repo.save(p)}

    fun addXp(amount:Long){
        val p=_player.value
        val xp=(p.xp+amount).coerceAtLeast(0)
        val level=ProgressionEngine.levelForXp(xp)
        update(p.copy(xp=xp,level=level,hp=p.stats.maxHp))
    }
    fun addStarterItem(){update(_player.value.copy(inventory=InventoryEngine.add(_player.value.inventory,Item("potion","Health Potion",1,25))))}
    fun togglePvp(){update(_player.value.copy(pvpEnabled=!_player.value.pvpEnabled))}
    fun addQuest(){if(_player.value.quests.none{it.id=="first"})update(_player.value.copy(quests=_player.value.quests+Quest("first","First Hunt",0,5)))}
    fun advanceQuest(){update(_player.value.copy(quests=QuestEngine.advance(_player.value.quests,"first",1)))}
    fun queue(){MatchmakingEngine.enqueue(MatchTicket("arena",_player.value.id))}
    fun createGuild(){if(_player.value.guild==null)update(_player.value.copy(guild=GuildEngine.create(_player.value.id,_player.value.name,"guild-local","Star Vanguard")))}
    fun createParty(){if(_player.value.party==null)update(_player.value.copy(party=PartyEngine.create("party-local",PartyMember(_player.value.id,_player.value.name,true))))}
    fun learnSkill(){update(_player.value.copy(skills=SkillEngine.learn(_player.value.skills,Skill("power-strike","Power Strike",1,10))))}
    fun trainSkill(){update(_player.value.copy(skills=SkillEngine.train(_player.value.skills,"power-strike")))}
    fun investTalent(){update(_player.value.copy(talents=TalentEngine.invest(if(_player.value.talents.isEmpty())listOf(Talent("strength","Strength Mastery"))else _player.value.talents,"strength")))}
    fun setFaction(){update(_player.value.copy(factions=FactionEngine.modify(_player.value.factions,"alliance","Alliance",100)))}
    fun equipStarter(){update(_player.value.copy(equipment=EquipmentEngine.equip(_player.value.equipment,Equipment("weapon","starter-sword","Starter Sword",10))))}
}
