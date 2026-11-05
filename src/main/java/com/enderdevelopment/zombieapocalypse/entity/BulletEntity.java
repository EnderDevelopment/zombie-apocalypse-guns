package com.enderdevelopment.zombieapocalypse.entity;

import com.enderdevelopment.zombieapocalypse.registry.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class BulletEntity extends ProjectileEntity {
    private int damage = 15;
    private int life = 60; // Ticks until despawn

    public BulletEntity(EntityType<? extends BulletEntity> entityType, World world) {
        super(entityType, world);
    }

    public BulletEntity(World world, LivingEntity owner, int damage) {
        super(ModEntities.BULLET, world);
        this.damage = damage;
        this.setOwner(owner);
        this.setPosition(owner.getX(), owner.getEyeY() - 0.1, owner.getZ());
    }

    @Override
    protected void initDataTracker() {
    }

    @Override
    public void tick() {
        super.tick();

        if (this.age > life) {
            this.discard();
            return;
        }

        // Move the bullet
        HitResult hitResult = ProjectileUtil.getCollision(this, this::canHit);
        if (hitResult.getType() != HitResult.Type.MISS) {
            this.onCollision(hitResult);
        }

        if (this.isRemoved()) {
            return;
        }

        this.setPosition(this.getX() + this.getVelocity().x, this.getY() + this.getVelocity().y, this.getZ() + this.getVelocity().z);

        if (this.getWorld().isClient) {
            spawnTrailParticles();
        }
    }

    private boolean canHit(net.minecraft.entity.Entity entity) {
        return entity.canHit() && entity != this.getOwner();
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (this.getWorld().isClient) {
            return;
        }
        net.minecraft.entity.Entity entity = entityHitResult.getEntity();
        entity.damage(this.getDamageSources().mobProjectile(this, (LivingEntity) this.getOwner()), damage);
        this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundEvents.BLOCK_STONE_HIT, net.minecraft.sound.SoundCategory.PLAYERS, 0.5f, 1.2f);
        this.discard();
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundEvents.BLOCK_STONE_HIT, net.minecraft.sound.SoundCategory.PLAYERS, 0.3f, 1.0f);
        this.discard();
    }

    private void spawnTrailParticles() {
        net.minecraft.particle.DustParticleEffect dust = new net.minecraft.particle.DustParticleEffect(
                net.minecraft.util.math.Vec3f(1.0f, 1.0f, 0.5f), 0.5f);
        this.getWorld().addParticle(dust, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
    }

    @Override
    protected float getDrag() {
        return 1.0f;
    }
}
