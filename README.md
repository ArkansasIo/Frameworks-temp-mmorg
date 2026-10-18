# Frameworks-temp-mmorg

A UE 5.7-ready, Blueprint-first RPG/MMORPG framework with an authoritative C++ gameplay core, data-driven systems, and optional C++23 domain layer.

## Included
- RPG/MMORPG stats and derived attributes
- Deterministic modifier pipeline
- Resources, combat, damage mitigation and critical hits
- Abilities, effects, buffs, debuffs and crowd control
- Inventory, equipment, itemization and loot interfaces
- Progression, classes, subclasses and talents
- Enemy archetypes, threat/aggro and boss encounters
- PvE/PvP rules and encounter scaling
- Data-driven JSON schemas and CSV registries
- Unreal Engine plugin structure and Blueprint integration
- Automated C++ tests and GitHub Actions validation

## Unreal compatibility
Unreal-facing modules use Unreal's supported C++ language baseline. Optional portable C++23 code is isolated under `Domain/` and is not required by UnrealHeaderTool.

## Layout
```text
Frameworks-temp-mmorg/
  Source/RPGMMOEngine/       Unreal runtime module
  Source/RPGMMOEditor/       Unreal editor module
  Domain/                    Portable C++23 domain layer
  Content/RPGMMO/            Blueprint/data architecture
  Config/                    Framework configuration
  Data/                      Registries and sample balance data
  Schemas/                   JSON schemas
  Docs/                      Architecture and system specifications
  Tests/                     Automated tests
```
