package com.arkansasio.rpgmmo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import com.arkansasio.rpgmmo.ui.RpgMmoScreen
import com.arkansasio.rpgmmo.ui.RpgMmoTheme

class MainActivity:ComponentActivity(){
    private val viewModel by viewModels<GameViewModel>()
    override fun onCreate(savedInstanceState:Bundle?){
        super.onCreate(savedInstanceState)
        setContent{RpgMmoTheme{
            val p=viewModel.player.collectAsState().value
            RpgMmoScreen(p,viewModel::addXp,viewModel::addStarterItem,viewModel::togglePvp,viewModel::addQuest,viewModel::advanceQuest,viewModel::queue,viewModel::createGuild,viewModel::createParty,viewModel::learnSkill,viewModel::trainSkill,viewModel::investTalent,viewModel::setFaction,viewModel::equipStarter)
        }}
    }
}
