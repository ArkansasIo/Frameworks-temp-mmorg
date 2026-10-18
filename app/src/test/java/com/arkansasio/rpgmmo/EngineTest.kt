package com.arkansasio.rpgmmo

import com.arkansasio.rpgmmo.domain.CombatEngine
import com.arkansasio.rpgmmo.domain.ProgressionEngine
import com.arkansasio.rpgmmo.model.Stats
import org.junit.Assert.assertEquals
import org.junit.Test

class EngineTest {
    @Test fun xpCurve(){assertEquals(500L,ProgressionEngine.requiredXp(2));assertEquals(16000L,ProgressionEngine.requiredXp(4))}
    @Test fun combat(){val r=CombatEngine.attack(Stats(str=20),Stats(con=10),0.1);assertEquals(40,r.damage)}
}
