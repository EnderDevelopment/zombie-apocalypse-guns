package com.enderdevelopment.zombieapocalypse.screen;

import com.enderdevelopment.zombieapocalypse.registry.ModScreenHandlers;
import com.enderdevelopment.zombieapocalypse.world.SurvivorTrades;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;

/**
 * Survivor trading screen.
 * Slot 0 = Payment (emeralds or food)
 * Slot 1 = Output (the item you receive)
 * The survivor consumes the payment and provides a random supply item.
 */
public class SurvivorTradingScreenHandler extends ScreenHandler {
    private final Inventory tradeInventory = new SimpleInventory(2) {
        @Override
        public void markDirty() {
            super.markDirty();
            updateOutput();
        }
    };

    public SurvivorTradingScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ModScreenHandlers.SURVIVOR_TRADING, syncId);

        // Payment slot (0)
        this.addSlot(new Slot(tradeInventory, 0, 53, 35) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return SurvivorTrades.isAcceptedPayment(stack);
            }
        });

        // Output slot (1)
        this.addSlot(new Slot(tradeInventory, 1, 107, 35) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }
        });

        // Player inventory
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }

    private void updateOutput() {
        ItemStack payment = tradeInventory.getStack(0);
        if (SurvivorTrades.isAcceptedPayment(payment)) {
            int value = SurvivorTrades.getPaymentValue(payment);
            if (value >= SurvivorTrades.MIN_TRADE_COST) {
                ItemStack result = SurvivorTrades.getRandomReward(payment.getItem(), value);
                tradeInventory.setStack(1, result);
                return;
            }
        }
        tradeInventory.setStack(1, ItemStack.EMPTY);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        // "Complete trade" button - consumes payment and moves reward to player
        if (id == 0) {
            ItemStack reward = tradeInventory.getStack(1);
            if (!reward.isEmpty()) {
                // Consume one payment unit
                ItemStack payment = tradeInventory.getStack(0);
                payment.decrement(1);
                if (payment.isEmpty()) {
                    tradeInventory.setStack(0, ItemStack.EMPTY);
                }
                player.getInventory().offerOrDrop(reward.copy());
                tradeInventory.setStack(1, ItemStack.EMPTY);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        // Return the payment if it wasn't consumed
        for (int i = 0; i < tradeInventory.size(); i++) {
            ItemStack stack = tradeInventory.getStack(i);
            if (!stack.isEmpty() && i == 0) {
                player.getInventory().offerOrDrop(stack);
            }
        }
        tradeInventory.clear();
    }
}
