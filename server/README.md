# MMORPG backend

This module is the authoritative server for the Android client. It is Kotlin/JVM + Ktor 3.6.0, MySQL, JSON REST, and WebSockets.

## Run

1. Create MySQL 8.0+ and a database named `rpgmmo`.
2. Set the variables in `.env.example` in the server process environment.
3. Run `./gradlew :server:run`.

The server applies the schema at startup and seeds a small three-node world graph. For manual database setup, use `server/sql/schema.sql`.

## API

- `POST /v1/auth/register`
- `POST /v1/auth/login`
- `POST /v1/auth/refresh`
- `POST /v1/characters`
- `POST /v1/sync`
- `POST /v1/world/move`
- `POST /v1/combat/attack`
- `POST /v1/guilds/mutate`
- `POST /v1/parties/mutate`
- `POST /v1/matchmaking/queue`
- `GET /v1/health`
- `WS /v1/realtime`

All protected HTTP routes require `Authorization: Bearer <access-token>`. Access tokens are short lived; refresh tokens are rotated and only a SHA-256 digest is persisted.

## Authority model

The server validates revisions, movement edges, combat requests, and synchronization operations. The Android client can predict locally, but server state is canonical. Critical writes use SQL transactions and optimistic revision checks.

Passwords are stored with PBKDF2-HMAC-SHA256 using 600,000 iterations and per-password random salts. Do not deploy with the example secret or without TLS termination. OWASP recommends slow, salted password hashing and short-lived bearer access tokens with refresh-token protection. 
