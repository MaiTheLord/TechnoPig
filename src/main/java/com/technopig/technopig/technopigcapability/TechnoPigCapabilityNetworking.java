package com.technopig.technopig.technopigcapability;

import com.technopig.technopig.TechnoPigMod;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = TechnoPigMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
final class TechnoPigCapabilityNetworking {
    private static final String PROTOCOL_VERSION = "1";

    private TechnoPigCapabilityNetworking() { }

    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(TechnoPigMod.MOD_ID, "technopigcapability"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    static void announceTechnoPigToClient(@NotNull Player player, @NotNull Pig pig) {
        CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> (ServerPlayer) player),
                new TechnoPigCapabilityNetworking.Packet(pig.getId())
        );
    }

    private static void register() {
        CHANNEL.messageBuilder(Packet.class, 0, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(Packet::encode)
                .decoder(Packet::decode)
                .consumer(Packet::handle)
                .add();
    }

    @SubscribeEvent
    static void enqueueRegister(@NotNull FMLCommonSetupEvent event) {
        event.enqueueWork(TechnoPigCapabilityNetworking::register);
    }

    private record Packet(int entityId) {
        private void encode(@NotNull FriendlyByteBuf buffer) {
            buffer.writeInt(entityId);
        }

        private static @NotNull Packet decode(@NotNull FriendlyByteBuf buf) {
            return new Packet(buf.readInt());
        }

        private boolean handle(@NotNull Supplier<NetworkEvent.Context> context) {
            AtomicBoolean success = new AtomicBoolean(false);

            context.get().enqueueWork(() -> {
                Level level = Minecraft.getInstance().level;
                if (level == null) return;

                if (!(level.getEntity(entityId) instanceof Pig pig)) return;

                TechnoPigCapability.setTechnoPig(pig);
                success.set(true);
            });

            context.get().setPacketHandled(true);
            return success.get();
        }
    }
}
