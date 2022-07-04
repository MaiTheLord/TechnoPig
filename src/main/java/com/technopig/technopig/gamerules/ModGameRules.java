package com.technopig.technopig.gamerules;

import com.technopig.technopig.TechnoPigMod;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Mod.EventBusSubscriber(modid = TechnoPigMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModGameRules {
    public static final GameRule.Boolean SPAWN_TECHNOPIGS = new GameRule.Boolean(
            "doSpawnTechnoPigs", true,
            "Spawn TechnoPigs", GameRules.Category.SPAWNING);

    public static final GameRule.Boolean TECHNOPIGS_DROP_SPECIAL_LOOT = new GameRule.Boolean(
            "doTechnoPigsDropSpecialLoot", true,
            "TechnoPigs drop special loot", GameRules.Category.DROPS);

    public static final GameRule.Boolean MAKE_ALL_PIGS_TECHNO_ON_TECHNO_DAY = new GameRule.Boolean(
            "doMakeAllPigsTechnoOnTechnobladeDay", true,
            "Make all pigs TechnoPigs on Technoblade day", GameRules.Category.MOBS);

    public static final GameRule.Integer TECHNO_PIG_SPAWN_CHANCE = new GameRule.Integer(
            "technoPigSpawnChance", 100,
            "TechnoPig spawn chance", GameRules.Category.SPAWNING);

    private static @NotNull List<GameRule<?, ?>> getGameRules() {
        return List.of(
                SPAWN_TECHNOPIGS,
                TECHNOPIGS_DROP_SPECIAL_LOOT,
                MAKE_ALL_PIGS_TECHNO_ON_TECHNO_DAY,
                TECHNO_PIG_SPAWN_CHANCE
        );
    }

    @SubscribeEvent
    static void registerGameRules(@NotNull FMLCommonSetupEvent event) {
        getGameRules().forEach(GameRule::register);
    }

    @SubscribeEvent
    static void addLanguageProvider(@NotNull GatherDataEvent event) {
        event.getGenerator().addProvider(event.includeClient(), new GameRule.LanguageProvider(event.getGenerator(), getGameRules()));
    }
}
