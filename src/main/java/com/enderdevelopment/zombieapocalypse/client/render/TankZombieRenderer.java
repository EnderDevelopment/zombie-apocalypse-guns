package com.enderdevelopment.zombieapocalypse.client.render;

import com.enderdevelopment.zombieapocalypse.entity.TankZombieEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ZombieEntityRenderer;
import net.minecraft.util.Identifier;

public class TankZombieRenderer extends ZombieEntityRenderer {
    private static final Identifier TEXTURE = new Identifier("minecraft", "textures/entity/zombie/husk.png");

    public TankZombieRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.shadowRadius = 1.0f;
    }

    @Override
    public Identifier getTexture(net.minecraft.entity.mob.ZombieEntity entity) {
        return TEXTURE;
    }
}
