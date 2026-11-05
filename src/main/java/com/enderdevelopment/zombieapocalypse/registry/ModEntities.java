package com.enderdevelopment.zombieapocalypse.registry;

import com.enderdevelopment.zombieapocalypse.ZombieApocalypseMod;
import com.enderdevelopment.zombieapocalypse.entity.*;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModEntities {
    public static final EntityType<FastZombieEntity> FAST_ZOMBIE = EntityType.Builder
            .create(FastZombieEntity::new, SpawnGroup.MONSTER)
            .dimensions(0.6f, 1.95f)
            .maxTrackingRange(8)
            .build();

    public static final EntityType<TankZombieEntity> TANK_ZOMBIE = EntityType.Builder
            .create(TankZombieEntity::new, SpawnGroup.MONSTER)
            .dimensions(1.1f, 2.6f)
            .maxTrackingRange(8)
            .build();

    public static final EntityType<SpitterZombieEntity> SPITTER_ZOMBIE = EntityType.Builder
            .create(SpitterZombieEntity::new, SpawnGroup.MONSTER)
            .dimensions(0.6f, 1.95f)
            .maxTrackingRange(8)
            .build();

    public static final EntityType<BruteZombieEntity> BRUTE_ZOMBIE = EntityType.Builder
            .create(BruteZombieEntity::new, SpawnGroup.MONSTER)
            .dimensions(0.8f, 2.3f)
            .maxTrackingRange(8)
            .build();

    public static final EntityType<SurvivorVillagerEntity> SURVIVOR = EntityType.Builder
            .create(SurvivorVillagerEntity::new, SpawnGroup.CREATURE)
            .dimensions(0.6f, 1.95f)
            .maxTrackingRange(10)
            .build();

    public static final EntityType<BulletEntity> BULLET = EntityType.Builder
            .create(BulletEntity::new, SpawnGroup.MISC)
            .dimensions(0.2f, 0.2f)
            .maxTrackingRange(4)
            .build();

    public static void registerModEntities() {
        Registry.register(Registries.ENTITY_TYPE, ZombieApocalypseMod.id("fast_zombie"), FAST_ZOMBIE);
        Registry.register(Registries.ENTITY_TYPE, ZombieApocalypseMod.id("tank_zombie"), TANK_ZOMBIE);
        Registry.register(Registries.ENTITY_TYPE, ZombieApocalypseMod.id("spitter_zombie"), SPITTER_ZOMBIE);
        Registry.register(Registries.ENTITY_TYPE, ZombieApocalypseMod.id("brute_zombie"), BRUTE_ZOMBIE);
        Registry.register(Registries.ENTITY_TYPE, ZombieApocalypseMod.id("survivor"), SURVIVOR);
        Registry.register(Registries.ENTITY_TYPE, ZombieApocalypseMod.id("bullet"), BULLET);

        FabricDefaultAttributeRegistry.register(FAST_ZOMBIE, FastZombieEntity.createFastZombieAttributes());
        FabricDefaultAttributeRegistry.register(TANK_ZOMBIE, TankZombieEntity.createTankZombieAttributes());
        FabricDefaultAttributeRegistry.register(SPITTER_ZOMBIE, SpitterZombieEntity.createSpitterZombieAttributes());
        FabricDefaultAttributeRegistry.register(BRUTE_ZOMBIE, BruteZombieEntity.createBruteZombieAttributes());
        FabricDefaultAttributeRegistry.register(SURVIVOR, SurvivorVillagerEntity.createSurvivorAttributes());
    }
}
