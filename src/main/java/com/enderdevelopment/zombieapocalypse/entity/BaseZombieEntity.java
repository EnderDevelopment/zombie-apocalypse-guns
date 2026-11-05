package com.enderdevelopment.zombieapocalypse.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public abstract class BaseZombieEntity extends ZombieEntity {
    public BaseZombieEntity(EntityType<? extends BaseZombieEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(6, new LookAroundGoal(this));
        this.goalSelector.add(7, new WanderAroundGoal(this, 1.0D));

        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, SurvivorVillagerEntity.class, true));
    }

    @Override
    public boolean canPickUpLoot() {
        return false;
    }

    @Override
    protected boolean burnsInDaylight() {
        return false;
    }

    public static DefaultAttributeContainer.Builder createBaseZombieAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 40.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0D)
                .add(EntityAttributes.GENERIC_ARMOR, 2.0D)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 0.1D);
    }

    @Override
    protected boolean isDisallowedInPeaceful() {
        return true;
    }

    // Give XP and loot
    @Override
    public int getXpToDrop() {
        return 10 + this.random.nextInt(15);
    }

    // Zombies burn? No - custom zombies don't burn in daylight
    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().isClient) {
            // Add a subtle rotting particle effect
            if (this.random.nextFloat() < 0.02f) {
                this.getWorld().addParticle(
                        net.minecraft.particle.ParticleTypes.ASH,
                        this.getX() + (this.random.nextDouble() - 0.5) * this.getWidth(),
                        this.getY() + this.getEyeHeight(),
                        this.getZ() + (this.random.nextDouble() - 0.5) * this.getWidth(),
                        0, 0.02, 0);
            }
        }
    }
}
