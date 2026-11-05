package com.enderdevelopment.zombieapocalypse.client.render;

import com.enderdevelopment.zombieapocalypse.entity.FastZombieEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ZombieEntityRenderer;
import net.minecraft.util.Identifier;

public class FastZombieRenderer extends ZombieEntityRenderer {
    private static final Identifier TEXTURE = new Identifier("minecraft", "textures/entity/zombie/zombie.png");

    public FastZombieRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(net.minecraft.entity.mob.ZombieEntity entity) {
        return TEXTURE;
    }
}
