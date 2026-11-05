package com.enderdevelopment.zombieapocalypse.client;

import com.enderdevelopment.zombieapocalypse.registry.ModEntities;
import com.enderdevelopment.zombieapocalypse.registry.ModScreenHandlers;
import com.enderdevelopment.zombieapocalypse.client.render.*;
import com.enderdevelopment.zombieapocalypse.client.screen.GunModifierScreen;
import com.enderdevelopment.zombieapocalypse.client.screen.SurvivorTradingScreen;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class ZombieApocalypseClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(ModScreenHandlers.GUN_MODIFIER, GunModifierScreen::new);
        HandledScreens.register(ModScreenHandlers.SURVIVOR_TRADING, SurvivorTradingScreen::new);

        net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register(
                ModEntities.FAST_ZOMBIE, FastZombieRenderer::new);
        net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register(
                ModEntities.TANK_ZOMBIE, TankZombieRenderer::new);
        net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register(
                ModEntities.SPITTER_ZOMBIE, SpitterZombieRenderer::new);
        net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register(
                ModEntities.BRUTE_ZOMBIE, BruteZombieRenderer::new);
        net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register(
                ModEntities.SURVIVOR, SurvivorVillagerRenderer::new);
        net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register(
                ModEntities.BULLET, BulletRenderer::new);
    }
}
