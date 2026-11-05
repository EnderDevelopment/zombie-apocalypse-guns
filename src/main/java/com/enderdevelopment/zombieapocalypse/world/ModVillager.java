package com.enderdevelopment.zombieapocalypse.world;

import com.enderdevelopment.zombieapocalypse.registry.ModEntities;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.BiomeKeys;

/**
 * Registers natural spawning of custom zombies and survivors across biomes,
 * and restores vanilla zombie spawning to be compatible with our customs.
 */
public class ModVillager {
    /**
     * Adds our custom zombies to the standard zombie spawn groups across all
     * land biomes where vanilla zombies spawn.
     */
    public static void registerAdditions() {
        // Custom zombies spawn everywhere vanilla zombies do
        BiomeModifications.addSpawn(
                BiomeSelectors.tag(net.minecraft.registry.tag.BiomeTags.IS_OVERWORLD),
                SpawnGroup.MONSTER,
                ModEntities.FAST_ZOMBIE,
                60, 2, 4
        );
        BiomeModifications.addSpawn(
                BiomeSelectors.tag(net.minecraft.registry.tag.BiomeTags.IS_OVERWORLD),
                SpawnGroup.MONSTER,
                ModEntities.TANK_ZOMBIE,
                15, 1, 2
        );
        BiomeModifications.addSpawn(
                BiomeSelectors.tag(net.minecraft.registry.tag.BiomeTags.IS_OVERWORLD),
                SpawnGroup.MONSTER,
                ModEntities.SPITTER_ZOMBIE,
                30, 1, 3
        );
        BiomeModifications.addSpawn(
                BiomeSelectors.tag(net.minecraft.registry.tag.BiomeTags.IS_OVERWORLD),
                SpawnGroup.MONSTER,
                ModEntities.BRUTE_ZOMBIE,
                20, 1, 2
        );

        // Survivors spawn in plains/villages and similar peaceful biomes
        BiomeModifications.addSpawn(
                BiomeSelectors.includeByKey(
                        BiomeKeys.PLAINS,
                        BiomeKeys.SUNFLOWER_PLAINS,
                        BiomeKeys.FOREST,
                        BiomeKeys.BIRCH_FOREST,
                        BiomeKeys.TAIGA,
                        BiomeKeys.MEADOW,
                        BiomeKeys.VILLAGE_PLAINS
                ),
                SpawnGroup.CREATURE,
                ModEntities.SURVIVOR,
                8, 1, 2
        );

        // Spawn restrictions
        SpawnRestriction.register(ModEntities.FAST_ZOMBIE,
                SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                net.minecraft.entity.mob.MobEntity::canMobSpawn);
        SpawnRestriction.register(ModEntities.TANK_ZOMBIE,
                SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                net.minecraft.entity.mob.MobEntity::canMobSpawn);
        SpawnRestriction.register(ModEntities.SPITTER_ZOMBIE,
                SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                net.minecraft.entity.mob.MobEntity::canMobSpawn);
        SpawnRestriction.register(ModEntities.BRUTE_ZOMBIE,
                SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                net.minecraft.entity.mob.MobEntity::canMobSpawn);
        SpawnRestriction.register(ModEntities.SURVIVOR,
                SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                net.minecraft.entity.passive.PassiveEntity::canMobSpawn);
    }

    /**
     * Placeholder to keep villager profession additions optional.
     * Survivors are standalone entities, so we keep this here for
     * potential future profession registration.
     */
    public static void registerTrades() {
        // Survivors use their own trade screen via SurvivorTradingScreenHandler.
        // This hook is reserved for future vanilla villager profession trades.
    }
}
