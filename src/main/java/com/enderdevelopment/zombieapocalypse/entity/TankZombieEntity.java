package com.enderdevelopment.zombieapocalypse.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.world.World;

/**
 * A massive, heavily armored zombie with high health and damage but slow speed.
 */
public class TankZombieEntity extends BaseZombieEntity {
    public TankZombieEntity(EntityType<? extends TankZombieEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer createTankZombieAttributes() {
        return createBaseZombieAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.16D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10.0D)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 80.0D)
                .add(EntityAttributes.GENERIC_ARMOR, 12.0D)
                .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 6.0D)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    public void tick() {
        super.tick();
        // Tanks have high damage resistance when below half health? No - give them a weakness:
        // They take fire damage more easily (they're enormous). Keep standard.
    }

    @Override
    public int getXpToDrop() {
        return 50 + this.random.nextInt(30);
    }
}
