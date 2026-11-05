package com.enderdevelopment.zombieapocalypse.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class AmmoItem extends Item {
    public enum AmmoType {
        PISTOL("Pistol Ammo"),
        HEAVY("Heavy Ammo"),
        RIFLE("Rifle Ammo"),
        SHELL("Shotgun Shells"),
        SMG("SMG Ammo");

        public final String displayName;

        AmmoType(String displayName) {
            this.displayName = displayName;
        }
    }

    private final AmmoType ammoType;

    public AmmoItem(AmmoType ammoType) {
        super(new Item.Settings().maxCount(64));
        this.ammoType = ammoType;
    }

    public AmmoType getAmmoType() {
        return ammoType;
    }

    @Override
    public Text getName(ItemStack stack) {
        return Text.literal(ammoType.displayName);
    }
}
