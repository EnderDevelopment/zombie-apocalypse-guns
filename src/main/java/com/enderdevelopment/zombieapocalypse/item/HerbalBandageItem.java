package com.enderdevelopment.zombieapocalypse.item;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class HerbalBandageItem extends Item {
    public HerbalBandageItem() {
        super(new Item.Settings().maxCount(16));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (user.getHealth() < user.getMaxHealth()) {
            user.heal(6.0f); // Heal 3 hearts
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 100, 0));
            if (!world.isClient) {
                world.playSound(null, user.getX(), user.getY(), user.getZ(),
                        net.minecraft.sound.SoundEvents.ITEM_HONEY_BOTTLE_DRINK,
                        net.minecraft.sound.SoundCategory.PLAYERS, 1.0f, 1.0f);
                stack.decrement(1);
            }
            return TypedActionResult.success(stack);
        }

        user.sendMessage(net.minecraft.text.Text.literal("You are already at full health!"), true);
        return TypedActionResult.fail(stack);
    }
}
