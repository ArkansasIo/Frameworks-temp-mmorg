package com.arkansasio.rpgmmo.server

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class SecurityTest {
    @Test fun passwordHashRoundTrip() {
        val hash=Passwords.hash("Correct-Horse-Battery-99")
        assertTrue(Passwords.verify("Correct-Horse-Battery-99",hash))
        assertFalse(Passwords.verify("wrong-password",hash))
    }

    @Test fun accessTokenRoundTrip() {
        val config=ServerConfig("127.0.0.1",8080,"jdbc:mysql://localhost/rpgmmo","u","p","01234567890123456789012345678901",900,86400)
        val token=Tokens.access("account-1",config)
        assertNotNull(Tokens.verifyAccess(token,config))
        assertTrue(Tokens.verifyAccess(token,config)=="account-1")
    }
}
