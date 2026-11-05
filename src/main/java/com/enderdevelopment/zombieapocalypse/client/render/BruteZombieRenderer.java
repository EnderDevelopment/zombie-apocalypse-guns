package com.enderdevelopment.zombieapocalypse.client.render;

import com.enderdevelopment.zombieapocalypse.entity.BruteZombieEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ZombieEntityRenderer;
import net.minecraft.util.Identifier;

public class BruteZombieRenderer extends ZombieEntityRenderer {
    private static final Identifier TEXTURE = new Identifier("minecraft", "textures/entity/zombie/zombie.png");

    public BruteZombieRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.shadowRadius = 0.8f;
    }

    @Override
    public Identifier getTexture(net.minecraft.entity.mob.ZombieEntity entity) {
        return TEXTURE;
    }
}
