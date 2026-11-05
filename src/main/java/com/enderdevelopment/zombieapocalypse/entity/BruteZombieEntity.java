package com.enderdevelopment.zombieapocalypse.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

/**
 * A heavy zombie with a club-like attack that inflicts weakness and knockback.
 */
public class BruteZombieEntity extends BaseZombieEntity {
    public BruteZombieEntity(EntityType<? extends BruteZombieEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer createBruteZombieAttributes() {
        return createBaseZombieAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.20D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 8.0D)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 50.0D)
                .add(EntityAttributes.GENERIC_ARMOR, 8.0D)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.5D)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.6D);
    }

    @Override
    public boolean tryAttack(net.minecraft.entity.Entity target) {
        boolean attacked = super.tryAttack(target);
        if (attacked && target instanceof PlayerEntity player) {
            // Inflict weakness and hunger on the target
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 100, 0));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 100, 0));
        }
        return attacked;
    }

    @Override
    public int getXpToDrop() {
        return 30 + this.random.nextInt(25);
    }
}
