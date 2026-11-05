package com.enderdevelopment.zombieapocalypse.client.render;

import com.enderdevelopment.zombieapocalypse.entity.BulletEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class BulletRenderer extends EntityRenderer<BulletEntity> {
    private static final Identifier TEXTURE = new Identifier("minecraft", "textures/block/iron_block.png");

    public BulletRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.shadowRadius = 0.0f;
    }

    @Override
    public void render(BulletEntity entity, float yaw, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        matrices.scale(0.15f, 0.15f, 0.15f);

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutout(TEXTURE));
        MatrixStack.Entry entry = matrices.peek();
        Matrix4f matrix4f = entry.getPositionMatrix();
        Matrix3f matrix3f = entry.getNormalMatrix();

        // Draw a small cube
        float size = 1.0f;
        for (Direction direction : Direction.values()) {
            renderFace(matrix4f, matrix3f, vertexConsumer, direction, size, light);
        }

        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    private void renderFace(Matrix4f matrix4f, Matrix3f normalMatrix, VertexConsumer vertexConsumer,
                            Direction direction, float size, int light) {
        float x1 = -size/2, x2 = size/2;
        float y1 = -size/2, y2 = size/2;
        float z1 = -size/2, z2 = size/2;

        float nx = direction.getOffsetX();
        float ny = direction.getOffsetY();
        float nz = direction.getOffsetZ();

        // Position the four corners based on direction
        for (int i = 0; i < 4; i++) {
            // Simple approach - use the cube corner coordinates
            float u = (i & 1) == 0 ? 0.0f : 1.0f;
            float v = (i & 2) == 0 ? 0.0f : 1.0f;
            // Determine which corner based on direction
            float px, py, pz;
            switch (direction) {
                case DOWN -> { px = (i & 1) == 0 ? x1 : x2; py = y1; pz = (i & 2) == 0 ? z1 : z2; }
                case UP -> { px = (i & 1) == 0 ? x1 : x2; py = y2; pz = (i & 2) == 0 ? z2 : z1; }
                case NORTH -> { px = (i & 1) == 0 ? x2 : x1; py = (i & 2) == 0 ? y1 : y2; pz = z1; }
                case SOUTH -> { px = (i & 1) == 0 ? x1 : x2; py = (i & 2) == 0 ? y1 : y2; pz = z2; }
                case WEST -> { px = x1; py = (i & 2) == 0 ? y1 : y2; pz = (i & 1) == 0 ? z2 : z1; }
                case EAST -> { px = x2; py = (i & 2) == 0 ? y1 : y2; pz = (i & 1) == 0 ? z1 : z2; }
                default -> { px = 0; py = 0; pz = 0; }
            }
            vertexConsumer.vertex(matrix4f, px, py, pz)
                    .color(255, 255, 255, 255)
                    .texture(u, v)
                    .overlay(OverlayTexture.DEFAULT_UV)
                    .light(light)
                    .normal(normalMatrix, nx, ny, nz)
                    .next();
        }
    }

    @Override
    public Identifier getTexture(BulletEntity entity) {
        return TEXTURE;
    }
}
