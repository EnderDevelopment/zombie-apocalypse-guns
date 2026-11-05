package com.enderdevelopment.zombieapocalypse.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.world.World;

/**
 * A fast, agile zombie that deals less damage but is very quick.
 */
public class FastZombieEntity extends BaseZombieEntity {
    public FastZombieEntity(EntityType<? extends FastZombieEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer createFastZombieAttributes() {
        return createBaseZombieAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.42D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0D)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 16.0D)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 2.0D);
    }

    @Override
    public void tick() {
        super.tick();
        // Speed boost when chasing
        if (this.getTarget() != null && !this.getWorld().isClient) {
            this.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.SPEED, 20, 1, true, false));
        }
    }
}
