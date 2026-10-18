# RPG MMO Android

Native Android/Kotlin RPG MMORPG client. The previous Unreal C++ source layer has been replaced by a mobile-first Android architecture using Kotlin, Jetpack Compose, ViewModel/state flow, local persistence, domain gameplay engines, and an authenticated MMO protocol client.

## Architecture
- `model` — player, stats, items, quests, skills, talents, guilds, parties, world and matchmaking models.
- `domain` — progression, combat, inventory, quests, crafting, equipment, factions, world and server-authority validation.
- `data` — local player persistence, account session storage, offline operation queue and WorkManager synchronization.
- `network` — HTTPS MMO client and versioned JSON protocol.
- `ui` — mobile Compose command interface.

The server is authoritative for movement, combat, inventory/economy transactions, guild/party mutations and matchmaking. The client may predict or queue non-critical actions, but it must never be trusted as the source of truth for rewards, damage, currency, inventory quantities or character progression.

## MMO API v1

| Endpoint | Purpose |
|---|---|
| `POST /v1/auth/register` | Create account/session |
| `POST /v1/auth/login` | Authenticate account |
| `POST /v1/auth/refresh` | Refresh session |
| `POST /v1/characters` | Character creation/selection |
| `POST /v1/sync` | Reconcile queued offline operations |
| `POST /v1/world/move` | Server-authoritative world movement |
| `POST /v1/combat/attack` | Server-authoritative combat action |
| `POST /v1/guilds/mutate` | Guild membership/mutation operations |
| `POST /v1/parties/mutate` | Party operations |
| `POST /v1/matchmaking/queue` | PvP/PvE matchmaking |

Synchronization carries a client revision, operation IDs, timestamps and base revisions so the backend can reject stale or invalid operations rather than blindly accepting client state.

## Offline synchronization

The local state remains usable while offline. Writes can be represented as durable operations in `SyncQueue`; `MmoSyncWorker` drains the queue when connectivity is available and retries transient failures with exponential backoff. The server remains the source of truth and returns accepted/rejected operation IDs plus the authoritative character state.

## Security

The MMO client requires HTTPS for API communication and disables cleartext traffic. Authentication uses bearer access tokens; passwords are never persisted by the client session store.

## Android build

The project targets Android API 37 with minSdk 23 and targetSdk 37. It uses AGP 9.3.0 / Gradle 9.5.0, the September 2026 Compose BOM, Kotlin serialization and WorkManager 2.12.0.

Run:

```text
:app:assembleDebug
:app:testDebugUnitTest
```

The application is intentionally mobile-first and does not depend on Unreal Engine, C++, or desktop-only APIs.
