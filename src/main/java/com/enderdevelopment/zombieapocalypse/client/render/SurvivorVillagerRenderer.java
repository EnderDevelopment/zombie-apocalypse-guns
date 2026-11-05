package com.enderdevelopment.zombieapocalypse.client.render;

import com.enderdevelopment.zombieapocalypse.entity.SurvivorVillagerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.VillagerEntityRenderer;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.util.Identifier;

public class SurvivorVillagerRenderer extends VillagerEntityRenderer {
    private static final Identifier TEXTURE = new Identifier("minecraft", "textures/entity/villager/villager.png");

    public SurvivorVillagerRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(VillagerEntity villagerEntity) {
        return TEXTURE;
    }
}
