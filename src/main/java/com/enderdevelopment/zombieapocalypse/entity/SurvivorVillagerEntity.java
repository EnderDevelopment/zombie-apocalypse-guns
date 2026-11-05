package com.enderdevelopment.zombieapocalypse.entity;

import com.enderdevelopment.zombieapocalypse.screen.SurvivorTradingScreenHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

/**
 * Survivor villagers are friendly NPCs that trade guns, ammo and supplies
 * for emeralds (or food). They fight back against zombies.
 * Extends VillagerEntity so it reuses the villager model/rendering.
 */
public class SurvivorVillagerEntity extends VillagerEntity {
    private String survivorName = "Survivor";

    public SurvivorVillagerEntity(EntityType<? extends SurvivorVillagerEntity> entityType, World world) {
        super(entityType, world);
        if (!world.isClient) {
            this.survivorName = generateName();
        }
    }

    private String generateName() {
        String[] firstNames = {"Marcus", "Sarah", "Riley", "Jordan", "Alex", "Dana", "Kai", "Nova"};
        String[] lastNames = {"The Hunter", "The Medic", "The Mechanic", "The Scout", "The Veteran", "The Runner"};
        return firstNames[this.random.nextInt(firstNames.length)]
                + " " + lastNames[this.random.nextInt(lastNames.length)];
    }

    public String getSurvivorName() {
        return survivorName;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.2D));
        this.goalSelector.add(4, new WanderAroundGoal(this, 0.6D));
        this.goalSelector.add(5, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(6, new LookAroundGoal(this));

        // Survivors fight back against zombies
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, BaseZombieEntity.class, true));
    }

    public static DefaultAttributeContainer createSurvivorAttributes() {
        return VillagerEntity.createVillagerAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 24.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.5D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0D);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (player.getWorld().isClient) {
            return ActionResult.success(true);
        }

        player.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                (syncId, inv, p) -> new SurvivorTradingScreenHandler(syncId, inv),
                Text.literal(survivorName + "'s Supplies")
        ));
        return ActionResult.CONSUME;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.getWorld().isClient && this.getTarget() != null) {
            if (this.squaredDistanceTo(this.getTarget()) < 4.0 && this.age % 20 == 0) {
                this.tryAttack(this.getTarget());
            }
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putString("SurvivorName", survivorName);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("SurvivorName")) {
            survivorName = nbt.getString("SurvivorName");
        }
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    // Survivors don't reproduce
    @Override
    public VillagerEntity createChild(ServerWorld world, VillagerEntity entity) {
        return null;
    }

    // Override to prevent zombies from infecting survivors
    @Override
    protected boolean canBeInfected() {
        return false;
    }
}
