package com.technopig.technopig.technopigcapability;

import com.technopig.technopig.TechnoPigMod;
import com.technopig.technopig.gamerules.ModGameRules;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber(modid = TechnoPigMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class TechnoPigCapability {
    static final Capability<Implementation> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {
    });

    private TechnoPigCapability() {
    }

    @SubscribeEvent
    static void register(@NotNull RegisterCapabilitiesEvent event) {
        event.register(Implementation.class);
    }

    public static boolean isTechnoPig(@Nullable Entity pig) {
        if (!(pig instanceof Pig)) return false;

        LazyOptional<Implementation> capability = pig.getCapability(TechnoPigCapability.INSTANCE);
        return capability.isPresent() && capability.resolve().orElseThrow().isTechnoPig();
    }

    static void setTechnoPig(@NotNull Pig pig) {
        LazyOptional<Implementation> capability = pig.getCapability(TechnoPigCapability.INSTANCE);
        capability.resolve().orElseThrow().setTechnoPig(true);
    }

    static final class Implementation {
        private final Level level;

        private Boolean is = null;

        Implementation(@NotNull Level level) {
            this.level = level;
        }

        boolean isTechnoPig() {
            if (is == null) is = !level.isClientSide && ModGameRules.SPAWN_TECHNOPIGS.get(level) && level.random.nextInt(ModGameRules.TECHNO_PIG_SPAWN_CHANCE.get(level)) == 0;

            return is;
        }

        void setTechnoPig(boolean to) {
            is = to;
        }
    }
}
