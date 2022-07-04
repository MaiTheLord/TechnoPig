<img src="https://github.com/MaiTheLord/TechnoPig/blob/main/src/main/resources/logo.png">

# TechnoPig
A Minecraft Forge mod made in honor of Technoblade.

The mod adds a TechnoPig pig variant to the game.

![2022-07-08_20 15 37](https://user-images.githubusercontent.com/63251610/178039850-6b4c116d-daa9-49ae-8539-bc0cea61e604.png)

## Spawning
When a regular pig spawns, there's a 1 in 100 chance that it will be a TechnoPig. the chance can be modified using the `technoPigSpawnChance` gamerule.
the spawn chance will then be 1 out of the value of `technoPigSpawnChance`.

TechnoPigs spawning can be completely disabled by setting the `doSpawnTechnoPigs` gamerule to false.

## Loot Drops
By default, TechnoPigs drop a potato in addition to the normal pig loot.
this behavior can be disabled by setting the `doTechnoPigsDropSpecialLoot` gamerule to false.

A Datapack can modify the exact drops of TechnoPigs, by overriding `data/technopig/loot_modifiers/technopig.json`.

## Technoblade Day *(Not Implemented Yet)*
All pigs become TechnoPigs for the entirety of Technoblade day (June 30th), according to the server's time and date.

This can be disabled by setting the `doMakeAllPigsTechnoOnTechnobladeDay` gamerule to false.
