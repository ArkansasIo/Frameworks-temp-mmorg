# Android Online/Offline MMORPG Architecture

The application is Android/Kotlin only. Gameplay state is kept locally so the mobile client remains usable when the network is unavailable. Network synchronization is isolated behind repositories/network clients.

## Layers

- UI: Jetpack Compose
- State: ViewModel + StateFlow
- Domain: progression, combat, inventory, quests, guilds, parties, factions, crafting, shops, world movement
- Local data: SharedPreferences-backed serialized player/session/queue state
- Network: HTTP API client and authentication API
- Background sync: WorkManager with a connected-network constraint

## Session

`SessionStore` holds the server-issued account identifier, display name, access token, and expiration timestamp. Passwords are not stored by the Android client.

## Synchronization

`SyncQueue` stores deferred operations locally. `MmoSyncWorker` drains the queue only when Android reports a network connection. Failed network operations return `Result.retry()`, allowing WorkManager to retry with its normal scheduling/backoff behavior.

For production MMO deployment, the backend remains authoritative for inventory, currency, combat outcomes, matchmaking, guild membership, and other security-sensitive state. The mobile client should treat local state as a cache and optimistic UI state rather than an authority.

Android's offline-first guidance recommends a local source of truth, repository-owned network synchronization, and WorkManager for persistent queued work. See https://developer.android.com/topic/architecture/data-layer/offline-first.
