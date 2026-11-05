package com.enderdevelopment.zombieapocalypse.world;

import com.enderdevelopment.zombieapocalypse.registry.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Handles trade logic for survivor villagers.
 * Survivors accept emeralds or food as payment and give
 * guns, ammo, attachments, and supplies in return.
 */
public class SurvivorTrades {
    public static final int MIN_TRADE_COST = 1;

    private static final Random RANDOM = new Random();

    // Trade table: item -> (unit cost, reward)
    private static final List<RewardEntry> REWARDS = new ArrayList<>();

    static {
        // Guns (expensive)
        REWARDS.add(new RewardEntry(ModItems.PISTOL, 8, 30));
        REWARDS.add(new RewardEntry(ModItems.REVOLVER, 20, 60));
        REWARDS.add(new RewardEntry(ModItems.RIFLE, 25, 80));
        REWARDS.add(new RewardEntry(ModItems.SHOTGUN, 18, 55));
        REWARDS.add(new RewardEntry(ModItems.SMG, 22, 70));

        // Ammo (cheap)
        REWARDS.add(new RewardEntry(ModItems.PISTOL_AMMO, 1, 12));
        REWARDS.add(new RewardEntry(ModItems.HEAVY_AMMO, 2, 8));
        REWARDS.add(new RewardEntry(ModItems.RIFLE_AMMO, 2, 10));
        REWARDS.add(new RewardEntry(ModItems.SHELLS, 2, 8));
        REWARDS.add(new RewardEntry(ModItems.SMG_AMMO, 2, 10));

        // Attachments
        REWARDS.add(new RewardEntry(ModItems.SCOPE, 12, 30));
        REWARDS.add(new RewardEntry(ModItems.SUPPRESSOR, 10, 25));
        REWARDS.add(new RewardEntry(ModItems.EXTENDED_MAG, 8, 20));
        REWARDS.add(new RewardEntry(ModItems.LASER_SIGHT, 6, 15));

        // Supplies
        REWARDS.add(new RewardEntry(ModItems.BANDAGE, 3, 10));
        REWARDS.add(new RewardEntry(Items.IRON_INGOT, 4, 12));
        REWARDS.add(new RewardEntry(Items.GOLDEN_APPLE, 12, 25));
        REWARDS.add(new RewardEntry(Items.BREAD, 1, 8));
    }

    public static boolean isAcceptedPayment(ItemStack stack) {
        return stack.isOf(Items.EMERALD)
                || stack.isOf(Items.BREAD)
                || stack.isOf(Items.COOKED_BEEF)
                || stack.isOf(Items.GOLDEN_APPLE)
                || stack.isOf(Items.IRON_INGOT);
    }

    public static int getPaymentValue(ItemStack stack) {
        if (stack.isOf(Items.EMERALD)) return 1;
        if (stack.isOf(Items.BREAD)) return 1;
        if (stack.isOf(Items.COOKED_BEEF)) return 2;
        if (stack.isOf(Items.GOLDEN_APPLE)) return 6;
        if (stack.isOf(Items.IRON_INGOT)) return 2;
        return 0;
    }

    public static ItemStack getRandomReward(Item paymentType, int paymentValue) {
        // Collect affordable rewards
        List<RewardEntry> affordable = new ArrayList<>();
        for (RewardEntry entry : REWARDS) {
            if (entry.minPaymentUnits <= paymentValue) {
                affordable.add(entry);
            }
        }

        if (affordable.isEmpty()) {
            // Give a consolation prize - always give at least bullets
            return new ItemStack(ModItems.PISTOL_AMMO, 4);
        }

        RewardEntry chosen = affordable.get(RANDOM.nextInt(affordable.size()));
        int amount = 1;
        // Ammo and supplies come in larger quantities
        if (chosen.item == ModItems.PISTOL_AMMO || chosen.item == ModItems.HEAVY_AMMO
                || chosen.item == ModItems.RIFLE_AMMO || chosen.item == ModItems.SHELLS
                || chosen.item == ModItems.SMG_AMMO) {
            amount = paymentValue * 2 + RANDOM.nextInt(6);
        } else if (chosen.item == Items.BREAD) {
            amount = paymentValue + RANDOM.nextInt(4);
        }

        return new ItemStack(chosen.item, amount);
    }

    private static class RewardEntry {
        final Item item;
        final int minPaymentUnits;
        final int weight;

        RewardEntry(Item item, int minPaymentUnits, int weight) {
            this.item = item;
            this.minPaymentUnits = minPaymentUnits;
            this.weight = weight;
        }
    }
}
