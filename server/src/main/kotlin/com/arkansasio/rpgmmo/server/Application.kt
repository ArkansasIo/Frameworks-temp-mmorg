package com.arkansasio.rpgmmo.server

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.http.*
import io.ktor.server.response.*
import kotlinx.serialization.json.Json

fun main() {
    val config = ServerConfig.fromEnvironment()
    val database = Database(config)
    database.migrate()
    embeddedServer(Netty, port = config.port, host = config.host) {
        module(config, database)
    }.start(wait = true)
}

fun Application.module(config: ServerConfig, database: Database) {
    install(CallLogging)
    install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true; encodeDefaults = true }) }
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            environment.log.error("Unhandled request failure", cause)
            call.respond(HttpStatusCode.InternalServerError, ErrorResponse("INTERNAL_ERROR"))
        }
    }
    RealtimeHub.install(this)
    ApiRoutes(this, config, database).register()
}

@kotlinx.serialization.Serializable
data class ErrorResponse(val errorCode: String, val message: String? = null)
