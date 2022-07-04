package com.technopig.technopig.technopigcapability;

import com.technopig.technopig.TechnoPigMod;
import net.minecraft.core.Direction;
import net.minecraft.nbt.ByteTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber(modid = TechnoPigMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
final class TechnoPigCapabilityAttacherAndSender {
    private static final ResourceLocation IDENTIFIER = new ResourceLocation(TechnoPigMod.MOD_ID, "technopig");

    private TechnoPigCapabilityAttacherAndSender() { }

    @SubscribeEvent
    static void attach(@NotNull AttachCapabilitiesEvent<Entity> event) {
        if (!(event.getObject() instanceof Pig pig)) return;

        event.addCapability(IDENTIFIER, new Provider(pig.level));
    }

    @SubscribeEvent
    static void sendToClient(@NotNull PlayerEvent.StartTracking event) {
        if (!(event.getTarget() instanceof Pig pig)) return;

        if (!TechnoPigCapability.isTechnoPig(pig)) return;

        TechnoPigCapabilityNetworking.announceTechnoPigToClient(event.getPlayer(), pig);
    }

    private static final class Provider implements ICapabilityProvider, INBTSerializable<ByteTag> {
        private final TechnoPigCapability.Implementation instance;

        private Provider(@NotNull Level level) {
            instance = new TechnoPigCapability.Implementation(level);
        }

        @Override
        public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return TechnoPigCapability.INSTANCE.orEmpty(cap, LazyOptional.of(() -> instance));
        }

        @Override
        public @NotNull ByteTag serializeNBT() {
            return ByteTag.valueOf(instance.isTechnoPig());
        }

        @Override
        public void deserializeNBT(@NotNull ByteTag byteTag) {
            instance.setTechnoPig(byteTag.getAsByte() != 0);
        }
    }
}
