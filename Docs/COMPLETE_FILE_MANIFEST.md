# Complete RPG MMORPG Framework File Manifest

This repository is intended to contain the complete framework specification and implementation tree.

## Unreal Engine runtime

- `RPGMMOEngine.uplugin`
- `Source/RPGMMOEngine/RPGMMOEngine.Build.cs`
- `Source/RPGMMOEngine/Public/RPGMMOEngine.h`
- `Source/RPGMMOEngine/Private/RPGMMOEngine.cpp`
- `Source/RPGMMOEngine/Public/Stats/RPGStatTypes.h`
- `Source/RPGMMOEngine/Public/Stats/RPGStatsComponent.h`
- `Source/RPGMMOEngine/Private/Stats/RPGStatsComponent.cpp`
- `Source/RPGMMOEngine/Public/Combat/RPGCombatTypes.h`
- `Source/RPGMMOEngine/Public/Combat/RPGCombatComponent.h`
- `Source/RPGMMOEngine/Private/Combat/RPGCombatComponent.cpp`
- `Source/RPGMMOEngine/Public/Resources/RPGResourceComponent.h`
- `Source/RPGMMOEngine/Private/Resources/RPGResourceComponent.cpp`
- `Source/RPGMMOEngine/Public/Abilities/RPGAbilityLibrary.h`
- `Source/RPGMMOEngine/Private/Abilities/RPGAbilityLibrary.cpp`

## Framework specification

- master statistics framework
- stat registry
- modifier engine
- combat formulas
- buff/debuff catalog
- PvE/PvP rulesets
- stat dependency graph
- stat test matrix
- itemization/equipment
- progression
- enemies/bosses/AI
- threat/aggro
- loot
- schemas and UML

## Planned implementation modules

Abilities, Effects, Inventory, Equipment, Items, Affixes, Gems, Enchantments, Progression, Classes, Talents, Quests, Crafting, Economy, Factions, Reputation, AI, Threat, Bosses, Loot, PvE, PvP, Parties, Guilds, Persistence, Networking, MMO services, telemetry, editor tooling and automated tests.