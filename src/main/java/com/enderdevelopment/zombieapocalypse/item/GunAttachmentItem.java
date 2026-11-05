package com.enderdevelopment.zombieapocalypse.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class GunAttachmentItem extends Item {
    public enum AttachmentType {
        SCOPE("Scope", "Reduces spread and increases accuracy"),
        SUPPRESSOR("Suppressor", "Silences gunfire"),
        EXTENDED_MAG("Extended Magazine", "Increases damage and capacity"),
        LASER_SIGHT("Laser Sight", "Improves accuracy");

        public final String displayName;
        public final String description;

        AttachmentType(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
    }

    private final AttachmentType attachmentType;

    public GunAttachmentItem(AttachmentType attachmentType) {
        super(new Item.Settings().maxCount(1));
        this.attachmentType = attachmentType;
    }

    public AttachmentType getAttachmentType() {
        return attachmentType;
    }

    public Attachment toAttachmentEnum() {
        return Attachment.valueOf(attachmentType.name());
    }

    @Override
    public Text getName(ItemStack stack) {
        return Text.literal(attachmentType.displayName);
    }
}
