package com.arkansasio.rpgmmo.server

class ServerConfig(
    val host: String,
    val port: Int,
    val jdbcUrl: String,
    val dbUser: String,
    val dbPassword: String,
    val jwtSecret: String,
    val accessTtlSeconds: Long,
    val refreshTtlSeconds: Long
) {
    companion object {
        fun fromEnvironment(): ServerConfig {
            fun required(name: String): String = System.getenv(name)?.takeIf { it.isNotBlank() }
                ?: error("Missing required environment variable: $name")
            return ServerConfig(
                host = System.getenv("MMO_HOST") ?: "0.0.0.0",
                port = (System.getenv("MMO_PORT") ?: "8080").toInt(),
                jdbcUrl = System.getenv("MMO_JDBC_URL") ?: "jdbc:mysql://127.0.0.1:3306/rpgmmo?useSSL=true&serverTimezone=UTC",
                dbUser = required("MMO_DB_USER"),
                dbPassword = required("MMO_DB_PASSWORD"),
                jwtSecret = required("MMO_JWT_SECRET").also { require(it.length >= 32) { "MMO_JWT_SECRET must be at least 32 characters" } },
                accessTtlSeconds = (System.getenv("MMO_ACCESS_TTL_SECONDS") ?: "900").toLong(),
                refreshTtlSeconds = (System.getenv("MMO_REFRESH_TTL_SECONDS") ?: "2592000").toLong()
            )
        }
    }
}
