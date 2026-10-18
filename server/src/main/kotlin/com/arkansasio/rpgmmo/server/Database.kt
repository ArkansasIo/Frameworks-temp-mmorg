package com.arkansasio.rpgmmo.server

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.util.UUID

class Database(private val config: ServerConfig) {
    init { Class.forName("com.mysql.cj.jdbc.Driver") }

    fun <T> transaction(block: (Connection) -> T): T {
        DriverManager.getConnection(config.jdbcUrl, config.dbUser, config.dbPassword).use { c ->
            c.autoCommit = false
            return try { block(c).also { c.commit() } } catch (e: Throwable) { c.rollback(); throw e }
        }
    }

    fun connection(): Connection = DriverManager.getConnection(config.jdbcUrl, config.dbUser, config.dbPassword)

    fun migrate() {
        connection().use { c ->
            c.createStatement().use { statement ->
                Schema.statements.forEach(statement::execute)
            }
        }
    }

    fun accountByEmail(email: String): Account? = connection().use { c ->
        c.prepareStatement("SELECT id,email,password_hash,created_at FROM accounts WHERE email=?").use { ps ->
            ps.setString(1, email.lowercase()); ps.executeQuery().use { rs -> if (rs.next()) rs.account() else null }
        }
    }

    fun createAccount(email: String, passwordHash: String): Account = transaction { c ->
        val id = UUID.randomUUID().toString()
        c.prepareStatement("INSERT INTO accounts(id,email,password_hash) VALUES(?,?,?)").use { ps ->
            ps.setString(1,id); ps.setString(2,email.lowercase()); ps.setString(3,passwordHash); ps.executeUpdate()
        }
        Account(id,email.lowercase(),passwordHash,null)
    }

    fun createSession(accountId: String, refreshHash: String, expiresAt: Long) = transaction { c ->
        c.prepareStatement("INSERT INTO sessions(id,account_id,refresh_hash,expires_at) VALUES(?,?,?,FROM_UNIXTIME(?))").use { ps ->
            ps.setString(1,UUID.randomUUID().toString()); ps.setString(2,accountId); ps.setString(3,refreshHash); ps.setLong(4,expiresAt); ps.executeUpdate()
        }
    }

    fun rotateRefresh(oldHash: String, newHash: String, expiresAt: Long): String? = transaction { c ->
        val sessionId = c.prepareStatement("SELECT id FROM sessions WHERE refresh_hash=? AND revoked_at IS NULL AND expires_at>UTC_TIMESTAMP() FOR UPDATE").use { ps -> ps.setString(1,oldHash); ps.executeQuery().use { if(it.next()) it.getString(1) else null } } ?: return@transaction null
        c.prepareStatement("UPDATE sessions SET refresh_hash=?,expires_at=FROM_UNIXTIME(?) WHERE id=?").use { ps -> ps.setString(1,newHash); ps.setLong(2,expiresAt); ps.setString(3,sessionId); ps.executeUpdate() }
        c.prepareStatement("SELECT account_id FROM sessions WHERE id=?").use { ps -> ps.setString(1,sessionId); ps.executeQuery().use { if(it.next()) it.getString(1) else null } }
    }

    fun player(characterId: String): PlayerRecord? = connection().use { c ->
        c.prepareStatement("SELECT id,account_id,name,class_id,race_id,level,xp,gold,hp,current_node,revision,player_json FROM characters WHERE id=?").use { ps -> ps.setString(1,characterId); ps.executeQuery().use { if(it.next()) it.player() else null } }
    }

    fun playerForAccount(accountId: String): PlayerRecord? = connection().use { c ->
        c.prepareStatement("SELECT id,account_id,name,class_id,race_id,level,xp,gold,hp,current_node,revision,player_json FROM characters WHERE account_id=? ORDER BY created_at LIMIT 1").use { ps -> ps.setString(1,accountId); ps.executeQuery().use { if(it.next()) it.player() else null } }
    }

    fun createCharacter(accountId:String,name:String,classId:String,raceId:String,playerJson:String):PlayerRecord = transaction { c ->
        val id=UUID.randomUUID().toString()
        c.prepareStatement("INSERT INTO characters(id,account_id,name,class_id,race_id,player_json) VALUES(?,?,?,?,?,?)").use{ps->ps.setString(1,id);ps.setString(2,accountId);ps.setString(3,name);ps.setString(4,classId);ps.setString(5,raceId);ps.setString(6,playerJson);ps.executeUpdate()}
        PlayerRecord(id,accountId,name,classId,raceId,1,0,1000,100,"starter",0,playerJson)
    }

    fun updatePlayer(c:Connection, id:String, revision:Long, name:String, level:Int, xp:Long, gold:Long, hp:Int, node:String, json:String):Boolean {
        c.prepareStatement("UPDATE characters SET name=?,level=?,xp=?,gold=?,hp=?,current_node=?,player_json=?,revision=revision+1,updated_at=UTC_TIMESTAMP() WHERE id=? AND revision=?").use { ps ->
            ps.setString(1,name);ps.setInt(2,level);ps.setLong(3,xp);ps.setLong(4,gold);ps.setInt(5,hp);ps.setString(6,node);ps.setString(7,json);ps.setString(8,id);ps.setLong(9,revision);return ps.executeUpdate()==1
        }
    }

    fun recordSync(c:Connection, operationId:String, characterId:String, baseRevision:Long, accepted:Boolean, conflict:String?) {
        c.prepareStatement("INSERT INTO sync_operations(id,character_id,base_revision,accepted,conflict) VALUES(?,?,?,?,?) ON DUPLICATE KEY UPDATE accepted=VALUES(accepted),conflict=VALUES(conflict)").use{ps->ps.setString(1,operationId);ps.setString(2,characterId);ps.setLong(3,baseRevision);ps.setBoolean(4,accepted);ps.setString(5,conflict);ps.executeUpdate()}
    }

    fun hasSyncOperation(c:Connection, operationId:String):Boolean = c.prepareStatement("SELECT 1 FROM sync_operations WHERE id=?").use{ps->ps.setString(1,operationId);ps.executeQuery().use(ResultSet::next)}

    private fun ResultSet.account()=Account(getString("id"),getString("email"),getString("password_hash"),getTimestamp("created_at"))
    private fun ResultSet.player()=PlayerRecord(getString("id"),getString("account_id"),getString("name"),getString("class_id"),getString("race_id"),getInt("level"),getLong("xp"),getLong("gold"),getInt("hp"),getString("current_node"),getLong("revision"),getString("player_json"))
}

data class Account(val id:String,val email:String,val passwordHash:String,val createdAt:java.sql.Timestamp?)
data class PlayerRecord(val id:String,val accountId:String,val name:String,val classId:String,val raceId:String,val level:Int,val xp:Long,val gold:Long,val hp:Int,val node:String,val revision:Long,val json:String)

object Schema {
    val statements = listOf(
        "CREATE TABLE IF NOT EXISTS accounts(id CHAR(36) PRIMARY KEY,email VARCHAR(320) NOT NULL UNIQUE,password_hash VARCHAR(255) NOT NULL,created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)",
        "CREATE TABLE IF NOT EXISTS sessions(id CHAR(36) PRIMARY KEY,account_id CHAR(36) NOT NULL,refresh_hash CHAR(64) NOT NULL UNIQUE,expires_at TIMESTAMP NOT NULL,revoked_at TIMESTAMP NULL,created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,FOREIGN KEY(account_id) REFERENCES accounts(id) ON DELETE CASCADE,INDEX idx_sessions_account(account_id),INDEX idx_sessions_expiry(expires_at))",
        "CREATE TABLE IF NOT EXISTS characters(id CHAR(36) PRIMARY KEY,account_id CHAR(36) NOT NULL,name VARCHAR(32) NOT NULL UNIQUE,class_id VARCHAR(32) NOT NULL,race_id VARCHAR(32) NOT NULL,level INT NOT NULL DEFAULT 1,xp BIGINT NOT NULL DEFAULT 0,gold BIGINT NOT NULL DEFAULT 1000,hp INT NOT NULL DEFAULT 100,current_node VARCHAR(64) NOT NULL DEFAULT 'starter',revision BIGINT NOT NULL DEFAULT 0,player_json JSON NOT NULL,created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,FOREIGN KEY(account_id) REFERENCES accounts(id) ON DELETE CASCADE,INDEX idx_characters_account(account_id))",
        "CREATE TABLE IF NOT EXISTS sync_operations(id VARCHAR(64) PRIMARY KEY,character_id CHAR(36) NOT NULL,base_revision BIGINT NOT NULL,accepted BOOLEAN NOT NULL,conflict VARCHAR(64) NULL,created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,FOREIGN KEY(character_id) REFERENCES characters(id) ON DELETE CASCADE)",
        "CREATE TABLE IF NOT EXISTS guilds(id CHAR(36) PRIMARY KEY,name VARCHAR(64) NOT NULL UNIQUE,leader_id CHAR(36) NOT NULL,created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)",
        "CREATE TABLE IF NOT EXISTS guild_members(guild_id CHAR(36) NOT NULL,character_id CHAR(36) NOT NULL,officer BOOLEAN NOT NULL DEFAULT FALSE,joined_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,PRIMARY KEY(guild_id,character_id),FOREIGN KEY(guild_id) REFERENCES guilds(id) ON DELETE CASCADE,FOREIGN KEY(character_id) REFERENCES characters(id) ON DELETE CASCADE)",
        "CREATE TABLE IF NOT EXISTS parties(id CHAR(36) PRIMARY KEY,leader_id CHAR(36) NOT NULL,created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)",
        "CREATE TABLE IF NOT EXISTS party_members(party_id CHAR(36) NOT NULL,character_id CHAR(36) NOT NULL,joined_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,PRIMARY KEY(party_id,character_id),FOREIGN KEY(party_id) REFERENCES parties(id) ON DELETE CASCADE,FOREIGN KEY(character_id) REFERENCES characters(id) ON DELETE CASCADE)",
        "CREATE TABLE IF NOT EXISTS matchmaking_tickets(id CHAR(36) PRIMARY KEY,character_id CHAR(36) NOT NULL,queue_name VARCHAR(32) NOT NULL,rating INT NOT NULL,created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,matched_at TIMESTAMP NULL,INDEX idx_match_queue(queue_name,rating,created_at),FOREIGN KEY(character_id) REFERENCES characters(id) ON DELETE CASCADE)",
        "CREATE TABLE IF NOT EXISTS economy_transactions(id CHAR(36) PRIMARY KEY,character_id CHAR(36) NOT NULL,kind VARCHAR(32) NOT NULL,amount BIGINT NOT NULL,balance_after BIGINT NOT NULL,reference_id VARCHAR(64) NULL,created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,FOREIGN KEY(character_id) REFERENCES characters(id) ON DELETE CASCADE,INDEX idx_economy_character(character_id,created_at))",
        "CREATE TABLE IF NOT EXISTS combat_events(id CHAR(36) PRIMARY KEY,character_id CHAR(36) NOT NULL,target_id VARCHAR(64) NOT NULL,ability_id VARCHAR(64) NOT NULL,damage INT NOT NULL,critical BOOLEAN NOT NULL,defeated BOOLEAN NOT NULL,created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,FOREIGN KEY(character_id) REFERENCES characters(id) ON DELETE CASCADE)",
        "CREATE TABLE IF NOT EXISTS world_nodes(id VARCHAR(64) PRIMARY KEY,name VARCHAR(128) NOT NULL,zone VARCHAR(64) NOT NULL,neighbors_json JSON NOT NULL)",
        "INSERT IGNORE INTO world_nodes(id,name,zone,neighbors_json) VALUES('starter','Starter Outpost','starter','[\"field_1\"]'),('field_1','Frontier Field','frontier','[\"starter\",\"field_2\"]'),('field_2','Ruined Gate','frontier','[\"field_1\"]')"
    )
}
