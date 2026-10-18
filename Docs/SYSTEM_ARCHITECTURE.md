# RPG MMORPG System Architecture

## Layers

1. Presentation / UI / Blueprint layer
2. Gameplay orchestration layer
3. Authoritative domain systems
4. Data/configuration layer
5. Persistence/network service interfaces
6. Optional portable C++23 domain utilities

## Runtime rules

- Server authoritative for combat, inventory, progression and economy.
- Balance values live in data assets/tables rather than gameplay code.
- Modifiers use deterministic priority and operation ordering.
- Every calculated combat value should be traceable for debugging.
- PvE and PvP rules are independently configurable.
- Content is versioned.

## Core pipeline

Input -> Intent -> Ability -> Effects -> Combat/Resource Resolution -> Replication -> UI.

## Major modules

Stats, Resources, Combat, Abilities, Effects, Inventory, Equipment, Items, Loot, Progression, Classes, Talents, Quests, Crafting, Economy, Factions, AI, Threat, Bosses, PvE/PvP, Party/Guild, Persistence and MMO services.