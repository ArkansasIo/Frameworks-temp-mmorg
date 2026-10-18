package com.arkansasio.rpgmmo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.arkansasio.rpgmmo.data.GameRepository
import com.arkansasio.rpgmmo.data.LocalStore
import com.arkansasio.rpgmmo.domain.MatchmakingEngine
import com.arkansasio.rpgmmo.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GameViewModel(app:Application):AndroidViewModel(app) {
    private val repo=GameRepository(LocalStore(app))
    private val _player=MutableStateFlow(repo.load())
    val player:StateFlow<Player> = _player
    private fun update(p:Player){_player.value=p;repo.save(p)}
    fun addXp(amount:Long){update(repo.grantXp(_player.value,amount))}
    fun addStarterItem(){update(repo.addItem(_player.value,Item("potion","Health Potion",1,25)))}
    fun togglePvp(){update(repo.togglePvp(_player.value))}
    fun addQuest(){update(_player.value.copy(quests=_player.value.quests+Quest("first","First Hunt",0,5)))}
    fun advanceQuest(){update(_player.value.copy(quests=com.arkansasio.rpgmmo.domain.QuestEngine.advance(_player.value.quests,"first",1)))}
    fun queue(){MatchmakingEngine.enqueue(MatchTicket("arena",_player.value.id))}
}
