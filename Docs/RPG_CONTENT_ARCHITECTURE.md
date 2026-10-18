# RPG MMORPG Content Architecture

The Android client now contains a data-driven foundation for a large persistent RPG/MMORPG world.

## World
- Biomes: plains, forest, desert, tundra, swamp, volcanic, mountain, coast, ruins and arcane.
- Zones contain level ranges, dimensions, world nodes, dungeon entrances and safe-zone flags.
- World maps contain named points for towns, dungeons and important locations.
- `ZoneEngine` validates recommended-level access.
- `MapEngine` calculates reachable world nodes.

## Dungeons
- Dungeon definitions support level ranges, room counts, boss IDs, party recommendations and loot tables.
- `DungeonEngine` generates deterministic room structures from definitions.
- Final rooms are boss chambers.

## Characters and equipment
- Equipment slots cover head, chest, legs, feet, hands, main/off hand, ring, amulet and back.
- Item rarity supports Common through Mythic.
- Item definitions contain requirements, stack limits, values, power, stat bonuses and icon IDs.
- Equipment loadouts can equip/unequip items and calculate total power.

## Inventory
- Fixed slot bags support stack merging, free-slot placement, removal and quantity queries.
- The existing player inventory remains compatible with the original game model while the new `InventoryBag` model supports a full mobile inventory UI.

## Content data
- `Data/world_zones.json` contains starter zones, biomes, map points and dungeons.
- `Data/equipment_catalog.json` contains gear and crafting recipes.
- Pixel-style Android vector assets are stored under `app/src/main/res/drawable/` for hero, enemies, world props, dungeon entrances and crystals.

## Mobile HUD
- Hero portrait/status panel.
- HP and XP bars.
- Gold display.
- Six-slot hotbar.
- Inventory/map/dungeon/hero taskbar controls.
- Inventory grid, world-map and dungeon-browser Compose screens.

This layer is intentionally data-driven so server-authoritative versions of the same definitions can later be loaded through the MMO API without replacing the Android UI architecture.
