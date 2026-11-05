package com.enderdevelopment.zombieapocalypse;

import com.enderdevelopment.zombieapocalypse.registry.ModItems;
import com.enderdevelopment.zombieapocalypse.registry.ModEntities;
import com.enderdevelopment.zombieapocalypse.registry.ModScreenHandlers;
import com.enderdevelopment.zombieapocalypse.world.ModVillager;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZombieApocalypseMod implements ModInitializer {
    public static final String MOD_ID = "zombieapocalypse";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModItems.registerModItems();
        ModEntities.registerModEntities();
        ModScreenHandlers.registerScreenHandlers();
        ModVillager.registerAdditions();
        ModVillager.registerTrades();

        LOGGER.info("Zombie Apocalypse mod loaded!");
    }

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }
}
