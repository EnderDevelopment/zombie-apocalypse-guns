package com.enderdevelopment.zombieapocalypse.registry;

import com.enderdevelopment.zombieapocalypse.ZombieApocalypseMod;
import com.enderdevelopment.zombieapocalypse.item.AmmoItem;
import com.enderdevelopment.zombieapocalypse.item.GunItem;
import com.enderdevelopment.zombieapocalypse.item.GunAttachmentItem;
import com.enderdevelopment.zombieapocalypse.item.HerbalBandageItem;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;

public class ModItems {
    private static final Map<String, Item> ITEMS = new HashMap<>();

    public static final Item PISTOL = new GunItem(GunItem.GunType.PISTOL);
    public static final Item REVOLVER = new GunItem(GunItem.GunType.REVOLVER);
    public static final Item RIFLE = new GunItem(GunItem.GunType.RIFLE);
    public static final Item SHOTGUN = new GunItem(GunItem.GunType.SHOTGUN);
    public static final Item SMG = new GunItem(GunItem.GunType.SMG);

    public static final Item PISTOL_AMMO = new AmmoItem(AmmoItem.AmmoType.PISTOL);
    public static final Item HEAVY_AMMO = new AmmoItem(AmmoItem.AmmoType.HEAVY);
    public static final Item RIFLE_AMMO = new AmmoItem(AmmoItem.AmmoType.RIFLE);
    public static final Item SHELLS = new AmmoItem(AmmoItem.AmmoType.SHELL);
    public static final Item SMG_AMMO = new AmmoItem(AmmoItem.AmmoType.SMG);

    public static final Item SCOPE = new GunAttachmentItem(GunAttachmentItem.AttachmentType.SCOPE);
    public static final Item SUPPRESSOR = new GunAttachmentItem(GunAttachmentItem.AttachmentType.SUPPRESSOR);
    public static final Item EXTENDED_MAG = new GunAttachmentItem(GunAttachmentItem.AttachmentType.EXTENDED_MAG);
    public static final Item LASER_SIGHT = new GunAttachmentItem(GunAttachmentItem.AttachmentType.LASER_SIGHT);

    public static final Item BANDAGE = new HerbalBandageItem();

    public static final ItemGroup ZOMBIE_APOCALYPSE_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(PISTOL))
            .displayName(Text.translatable("itemgroup.zombieapocalypse"))
            .entries((displayContext, entries) -> {
                entries.add(PISTOL);
                entries.add(REVOLVER);
                entries.add(RIFLE);
                entries.add(SHOTGUN);
                entries.add(SMG);
                entries.add(PISTOL_AMMO);
                entries.add(HEAVY_AMMO);
                entries.add(RIFLE_AMMO);
                entries.add(SHELLS);
                entries.add(SMG_AMMO);
                entries.add(SCOPE);
                entries.add(SUPPRESSOR);
                entries.add(EXTENDED_MAG);
                entries.add(LASER_SIGHT);
                entries.add(BANDAGE);
            })
            .build();

    public static void registerModItems() {
        Registry.register(Registries.ITEM_GROUP, ZombieApocalypseMod.id("zombie_apocalypse"), ZOMBIE_APOCALYPSE_GROUP);

        register("pistol", PISTOL);
        register("revolver", REVOLVER);
        register("rifle", RIFLE);
        register("shotgun", SHOTGUN);
        register("smg", SMG);

        register("pistol_ammo", PISTOL_AMMO);
        register("heavy_ammo", HEAVY_AMMO);
        register("rifle_ammo", RIFLE_AMMO);
        register("shells", SHELLS);
        register("smg_ammo", SMG_AMMO);

        register("scope", SCOPE);
        register("suppressor", SUPPRESSOR);
        register("extended_mag", EXTENDED_MAG);
        register("laser_sight", LASER_SIGHT);

        register("bandage", BANDAGE);
    }

    private static void register(String name, Item item) {
        Registry.register(Registries.ITEM, ZombieApocalypseMod.id(name), item);
        ITEMS.put(name, item);
    }

    public static Item getItem(String name) {
        return ITEMS.get(name);
    }
}
