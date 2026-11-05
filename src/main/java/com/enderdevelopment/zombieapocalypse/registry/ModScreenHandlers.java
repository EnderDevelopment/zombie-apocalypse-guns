package com.enderdevelopment.zombieapocalypse.registry;

import com.enderdevelopment.zombieapocalypse.ZombieApocalypseMod;
import com.enderdevelopment.zombieapocalypse.screen.GunModifierScreenHandler;
import com.enderdevelopment.zombieapocalypse.screen.SurvivorTradingScreenHandler;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;

public class ModScreenHandlers {
    public static ScreenHandlerType<GunModifierScreenHandler> GUN_MODIFIER;
    public static ScreenHandlerType<SurvivorTradingScreenHandler> SURVIVOR_TRADING;

    public static void registerScreenHandlers() {
        GUN_MODIFIER = Registry.register(
                Registries.SCREEN_HANDLER,
                ZombieApocalypseMod.id("gun_modifier"),
                new ScreenHandlerType<>(GunModifierScreenHandler::new, FeatureFlags.VANILLA_FEATURES)
        );

        SURVIVOR_TRADING = Registry.register(
                Registries.SCREEN_HANDLER,
                ZombieApocalypseMod.id("survivor_trading"),
                new ScreenHandlerType<>(SurvivorTradingScreenHandler::new, FeatureFlags.VANILLA_FEATURES)
        );
    }
}
