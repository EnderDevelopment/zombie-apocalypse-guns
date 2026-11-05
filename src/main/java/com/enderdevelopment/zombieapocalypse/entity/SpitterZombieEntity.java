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
 * A ranged zombie that spits toxic acid at the player, dealing poison damage.
 */
public class SpitterZombieEntity extends BaseZombieEntity {
    private int spitCooldown = 60;
    private static final int SPIT_RANGE = 20;

    public SpitterZombieEntity(EntityType<? extends SpitterZombieEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer createSpitterZombieAttributes() {
        return createBaseZombieAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.22D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0D)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 24.0D);
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity target = this.getTarget();
        if (target == null || this.getWorld().isClient) {
            return;
        }

        if (this.squaredDistanceTo(target) < SPIT_RANGE * SPIT_RANGE) {
            if (spitCooldown <= 0) {
                spitAcid(target);
                spitCooldown = 80;
            } else {
                spitCooldown--;
            }
        }
    }

    private void spitAcid(LivingEntity target) {
        // Simple ranged acid attack that applies poison
        if (target instanceof PlayerEntity) {
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 100, 0));
            target.damage(this.getDamageSources().mobAttack(this), 2.0f);

            this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(),
                    net.minecraft.sound.SoundEvents.ENTITY_SLIME_SQUISH,
                    net.minecraft.sound.SoundCategory.HOSTILE, 1.0f, 0.5f);

            if (!this.getWorld().isClient) {
                // Add green particles at target
                for (int i = 0; i < 10; i++) {
                    this.getWorld().addParticle(
                            net.minecraft.particle.ParticleTypes.ITEM_SLIME,
                            target.getX(), target.getY() + 1.0, target.getZ(),
                            (this.random.nextDouble() - 0.5) * 0.2,
                            0.1,
                            (this.random.nextDouble() - 0.5) * 0.2);
                }
            }
        }
    }

    @Override
    public int getXpToDrop() {
        return 20 + this.random.nextInt(20);
    }
}
