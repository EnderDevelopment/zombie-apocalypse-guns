package com.enderdevelopment.zombieapocalypse.screen;

import com.enderdevelopment.zombieapocalypse.item.Attachment;
import com.enderdevelopment.zombieapocalypse.item.GunAttachmentItem;
import com.enderdevelopment.zombieapocalypse.item.GunItem;
import com.enderdevelopment.zombieapocalypse.registry.ModScreenHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

/**
 * A screen handler for modifying guns with attachments.
 * Slot 0 = The gun to modify
 * Slot 1 = The attachment to apply
 * Slot 2 = Output (result)
 */
public class GunModifierScreenHandler extends ScreenHandler {
    private final Inventory modifierInventory = new SimpleInventory(3) {
        @Override
        public void markDirty() {
            super.markDirty();
            updateOutput();
        }
    };
    private final PlayerInventory playerInventory;

    public GunModifierScreenHandler(int syncId, PlayerInventory playerInventory, ItemStack gunStack) {
        super(ModScreenHandlers.GUN_MODIFIER, syncId);
        this.playerInventory = playerInventory;
        modifierInventory.setStack(0, gunStack);

        // Gun slot (0)
        this.addSlot(new Slot(modifierInventory, 0, 26, 35) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.getItem() instanceof GunItem;
            }
        });

        // Attachment slot (1)
        this.addSlot(new Slot(modifierInventory, 1, 62, 35) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.getItem() instanceof GunAttachmentItem;
            }
        });

        // Output slot (2)
        this.addSlot(new Slot(modifierInventory, 2, 98, 35) {
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
        // Player hotbar
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }

    private void updateOutput() {
        ItemStack gun = modifierInventory.getStack(0);
        ItemStack attachment = modifierInventory.getStack(1);

        if (gun.getItem() instanceof GunItem && attachment.getItem() instanceof GunAttachmentItem) {
            GunAttachmentItem attItem = (GunAttachmentItem) attachment.getItem();
            Attachment att = attItem.toAttachmentEnum();

            if (!GunItem.hasAttachment(gun, att)) {
                ItemStack result = gun.copy();
                GunItem.addAttachment(result, att);
                modifierInventory.setStack(2, result);
                return;
            }
        }
        modifierInventory.setStack(2, ItemStack.EMPTY);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slotObject = this.slots.get(slot);
        if (slotObject != null && slotObject.hasStack()) {
            ItemStack stackInSlot = slotObject.getStack();
            itemStack = stackInSlot.copy();

            // If from modifier area (0-2), move to player inventory
            if (slot < 3) {
                if (!this.insertItem(stackInSlot, 3, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // From player inventory, try to insert into gun or attachment slot
                if (stackInSlot.getItem() instanceof GunItem && this.insertItem(stackInSlot, 0, 1, false)) {
                    // inserted
                } else if (stackInSlot.getItem() instanceof GunAttachmentItem && !this.getSlot(1).hasStack()) {
                    if (!this.insertItem(stackInSlot, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    return ItemStack.EMPTY;
                }
            }

            if (stackInSlot.isEmpty()) {
                slotObject.setStack(ItemStack.EMPTY);
            } else {
                slotObject.markDirty();
            }
        }
        return itemStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        // Return any items left in the modifier slots back to the player
        for (int i = 0; i < modifierInventory.size(); i++) {
            ItemStack stack = modifierInventory.getStack(i);
            if (!stack.isEmpty()) {
                player.getInventory().offerOrDrop(stack);
            }
        }
        modifierInventory.clear();
    }
}
