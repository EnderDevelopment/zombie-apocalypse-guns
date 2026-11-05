package com.enderdevelopment.zombieapocalypse.item;

import com.enderdevelopment.zombieapocalypse.entity.BulletEntity;
import com.enderdevelopment.zombieapocalypse.registry.ModItems;
import com.enderdevelopment.zombieapocalypse.registry.ModScreenHandlers;
import com.enderdevelopment.zombieapocalypse.screen.GunModifierScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class GunItem extends Item {
    public enum GunType {
        PISTOL("Pistol", 12, 15, 0.45f, 6.0f, AmmoItem.AmmoType.PISTOL, 2.0f, 20, 100),
        REVOLVER("Revolver", 6, 40, 0.30f, 2.5f, AmmoItem.AmmoType.HEAVY, 3.0f, 25, 120),
        RIFLE("Rifle", 20, 22, 0.25f, 8.0f, AmmoItem.AmmoType.RIFLE, 2.2f, 30, 180),
        SHOTGUN("Shotgun", 8, 25, 0.70f, 4.0f, AmmoItem.AmmoType.SHELL, 1.8f, 12, 90),
        SMG("SMG", 30, 12, 0.08f, 5.0f, AmmoItem.AmmoType.SMG, 1.5f, 15, 80);

        public final String displayName;
        public final int magSize;
        public final int damage;
        public final float reloadTime;
        public final float fireRate;
        public final AmmoItem.AmmoType ammoType;
        public final float spread;
        public final int range;
        public final int basePrice;

        GunType(String displayName, int magSize, int damage, float fireRate, float reloadTime,
                AmmoItem.AmmoType ammoType, float spread, int range, int basePrice) {
            this.displayName = displayName;
            this.magSize = magSize;
            this.damage = damage;
            this.fireRate = fireRate;
            this.reloadTime = reloadTime;
            this.ammoType = ammoType;
            this.spread = spread;
            this.range = range;
            this.basePrice = basePrice;
        }
    }

    private final GunType gunType;

    public static final String NBT_KEY_AMMO = "current_ammo";
    public static final String NBT_KEY_LAST_SHOT = "last_shot";
    public static final String NBT_KEY_RELOADING_UNTIL = "reloading_until";
    public static final String NBT_KEY_ATTACHMENTS = "attachments";

    public GunItem(GunType gunType) {
        super(new Item.Settings().maxCount(1));
        this.gunType = gunType;
    }

    public GunType getGunType() {
        return gunType;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (world.isClient) {
            return TypedActionResult.pass(stack);
        }

        // Open modification screen when sneaking
        if (user.isSneaking()) {
            user.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                    (syncId, inv, player) -> new GunModifierScreenHandler(syncId, inv, stack),
                    Text.literal("Gun Modification")
            ));
            return TypedActionResult.success(stack);
        }

        int ammoLeft = getCurrentAmmo(stack);
        long worldTime = world.getTime();
        long lastShot = stack.getOrCreateNbt().getLong(NBT_KEY_LAST_SHOT);
        long reloadingUntil = stack.getOrCreateNbt().getLong(NBT_KEY_RELOADING_UNTIL);

        // If reloading, don't fire
        if (worldTime < reloadingUntil) {
            user.sendMessage(Text.literal("Reloading..."), true);
            return TypedActionResult.fail(stack);
        }

        // Check fire rate
        if (worldTime - lastShot < getFireRateTicks()) {
            return TypedActionResult.fail(stack);
        }

        // If out of ammo, try to reload automatically
        if (ammoLeft <= 0) {
            if (tryReload(world, user, stack)) {
                return TypedActionResult.success(stack);
            }
            user.sendMessage(Text.literal("Out of ammo! No ammo in inventory."), true);
            return TypedActionResult.fail(stack);
        }

        // Fire!
        fire(world, user, stack);
        stack.getOrCreateNbt().putInt(NBT_KEY_AMMO, ammoLeft - 1);
        stack.getOrCreateNbt().putLong(NBT_KEY_LAST_SHOT, worldTime);

        // Auto reload when empty
        if (ammoLeft - 1 <= 0) {
            tryReload(world, user, stack);
        }

        return TypedActionResult.success(stack);
    }

    private long getFireRateTicks() {
        return (long) (gunType.fireRate * 20);
    }

    private void fire(World world, PlayerEntity user, ItemStack stack) {
        float spread = gunType.spread;

        // Calculate spread with attachments
        if (hasAttachment(stack, Attachment.valueOf("LASER_SIGHT"))) {
            spread *= 0.6f;
        }

        // Determine number of pellets (shotgun fires multiple)
        int pellets = gunType == GunType.SHOTGUN ? 5 : 1;
        int bulletDamage = gunType.damage;

        if (hasAttachment(stack, Attachment.valueOf("EXTENDED_MAG"))) {
            bulletDamage += 2;
        }

        for (int i = 0; i < pellets; i++) {
            BulletEntity bullet = new BulletEntity(world, user, bulletDamage);
            bullet.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 3.0f, spread * 10.0f);
            world.spawnEntity(bullet);
        }

        // Recoil
        Vec3d look = user.getRotationVec(1.0f);
        user.addVelocity(look.x * -0.15, 0.05, look.z * -0.15);
        user.velocityModified = true;

        // Suppressor reduces sound radius
        boolean suppressed = hasAttachment(stack, Attachment.valueOf("SUPPRESSOR"));
        float volume = suppressed ? 0.2f : 1.0f;
        world.playSound(null, user.getX(), user.getY(), user.getZ(),
                net.minecraft.sound.SoundEvents.ENTITY_GENERIC_EXPLODE,
                net.minecraft.sound.SoundCategory.PLAYERS, volume, 0.8f);
    }

    private boolean tryReload(World world, PlayerEntity user, ItemStack gun) {
        ItemStack ammoStack = findAmmo(user, gunType.ammoType);
        if (ammoStack.isEmpty()) {
            return false;
        }

        int totalAmmo = ammoStack.getCount();
        int needed = gunType.magSize - getCurrentAmmo(gun);
        int toReload = Math.min(totalAmmo, needed);
        if (toReload <= 0) {
            return false;
        }

        ammoStack.decrement(toReload);
        int newAmmo = getCurrentAmmo(gun) + toReload;
        gun.getOrCreateNbt().putInt(NBT_KEY_AMMO, newAmmo);
        gun.getOrCreateNbt().putLong(NBT_KEY_RELOADING_UNTIL, world.getTime() + (long) (gunType.reloadTime * 20));
        user.sendMessage(Text.literal("Reloading..."), true);
        return true;
    }

    private ItemStack findAmmo(PlayerEntity user, AmmoItem.AmmoType type) {
        for (int i = 0; i < user.getInventory().size(); i++) {
            ItemStack stack = user.getInventory().getStack(i);
            if (stack.getItem() instanceof AmmoItem ammoItem && ammoItem.getAmmoType() == type) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    public static int getCurrentAmmo(ItemStack stack) {
        return stack.getOrCreateNbt().getInt(NBT_KEY_AMMO);
    }

    public static boolean hasAttachment(ItemStack stack, Attachment attachment) {
        if (!stack.hasNbt() || !stack.getNbt().contains(NBT_KEY_ATTACHMENTS)) {
            return false;
        }
        int[] attachments = stack.getNbt().getIntArray(NBT_KEY_ATTACHMENTS);
        for (int a : attachments) {
            if (a == attachment.ordinal()) {
                return true;
            }
        }
        return false;
    }

    public static List<Attachment> getAttachments(ItemStack stack) {
        List<Attachment> list = new ArrayList<>();
        if (stack.hasNbt() && stack.getNbt().contains(NBT_KEY_ATTACHMENTS)) {
            int[] attachments = stack.getNbt().getIntArray(NBT_KEY_ATTACHMENTS);
            for (int a : attachments) {
                list.add(Attachment.values()[a]);
            }
        }
        return list;
    }

    public static void addAttachment(ItemStack stack, Attachment attachment) {
        int[] current = stack.getOrCreateNbt().getIntArray(NBT_KEY_ATTACHMENTS);
        int[] updated = new int[current.length + 1];
        System.arraycopy(current, 0, updated, 0, current.length);
        updated[current.length] = attachment.ordinal();
        stack.getNbt().putIntArray(NBT_KEY_ATTACHMENTS, updated);
    }

    @Override
    public Text getName(ItemStack stack) {
        return Text.literal(gunType.displayName);
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<net.minecraft.text.Text> tooltip, net.minecraft.client.item.TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        // Show ammo count
        int ammo = getCurrentAmmo(stack);
        tooltip.add(Text.literal("Ammo: " + ammo + "/" + gunType.magSize));
        tooltip.add(Text.literal("Damage: " + gunType.damage));

        // Show attachments
        List<Attachment> attachments = getAttachments(stack);
        if (!attachments.isEmpty()) {
            StringBuilder sb = new StringBuilder("Attachments: ");
            for (int i = 0; i < attachments.size(); i++) {
                if (i > 0) sb.append(", ");
                sb.append(attachments.get(i).name());
            }
            tooltip.add(Text.literal(sb.toString()));
        } else {
            tooltip.add(Text.literal("Attachments: none"));
        }
        tooltip.add(Text.literal("Sneak + right click to modify"));
    }
}
