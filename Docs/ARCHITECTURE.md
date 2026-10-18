# RPG/MMORPG Framework Architecture

## Runtime pipeline
Input -> Intent -> Ability -> Effects -> Combat Resolution -> Resources -> Replication -> UI.

## Server authority
The server owns combat outcomes, inventory, progression, loot, economy and persistent state. Clients submit intents and receive replicated results.

## Systems
Stats, modifiers, resources, abilities, effects, inventory, equipment, itemization, progression, classes, talents, AI, threat, bosses, loot, quests, crafting, economy, factions, PvE/PvP, parties, guilds, persistence and telemetry.

## Data-driven rule
Balance values live in DataAssets/DataTables/JSON registries rather than gameplay code. Every calculation should be deterministic and testable.
