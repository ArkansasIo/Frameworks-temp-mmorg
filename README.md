# RPG MMO Android

Native Android/Kotlin conversion of the RPG MMO framework. The previous Unreal C++ source layer has been replaced by an Android application architecture using Kotlin, Jetpack Compose, ViewModel/state flow, local persistence, domain gameplay engines, and an HTTP MMO client.

## Modules
- `app/src/main/java/com/arkansasio/rpgmmo/model` — player, stats, items, quests, skills, talents, matchmaking models.
- `domain` — combat, progression, inventory, quests, matchmaking rules.
- `data` — local player persistence and repository.
- `network` — Android HTTP client for an MMO backend.
- `ui` — adaptive Compose-based command UI.

The project targets Android API 37 with minSdk 23. Android's current guidance recommends layered UI/data architecture, ViewModels, coroutines/flows, and Jetpack Compose for modern applications. See the Android architecture guidance for the rationale. The project uses AGP 9.3.0 / Gradle 9.5.0 and the September 2026 Compose BOM.

## Build
Open the repository in Android Studio and sync Gradle. Then run `:app:assembleDebug` or `:app:testDebugUnitTest`.

The application is intentionally mobile-first and does not depend on Unreal Engine, C++, or desktop-only APIs.
