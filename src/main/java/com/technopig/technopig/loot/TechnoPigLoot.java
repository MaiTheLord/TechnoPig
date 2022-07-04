package com.technopig.technopig.loot;

import com.google.gson.JsonObject;
import com.technopig.technopig.TechnoPigMod;
import com.technopig.technopig.gamerules.ModGameRules;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = TechnoPigMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
final class TechnoPigLoot {
    private static RegistryObject<Modifier.Serializer> serializer;

    private TechnoPigLoot() { }

    @SubscribeEvent
    static void registerSerializer(@NotNull RegisterEvent event) {
        final ResourceLocation serializerLocation = new ResourceLocation(TechnoPigMod.MOD_ID, "technopig");

        event.register(ForgeRegistries.Keys.LOOT_MODIFIER_SERIALIZERS,
                helper -> helper.register(serializerLocation, new Modifier.Serializer())
        );

        serializer = RegistryObject.create(serializerLocation, ForgeRegistries.LOOT_MODIFIER_SERIALIZERS.get());
    }

    @SubscribeEvent
    static void addDataProvider(@NotNull GatherDataEvent event) {
        event.getGenerator().addProvider(event.includeServer(), new DataProvider(event.getGenerator()));
    }

    private static final class Modifier extends LootModifier {
        private final Item item;
        private final int amount;
        private final boolean override;

        private Modifier(LootItemCondition[] conditionsIn, @NotNull Item item, int amount, boolean override) {
            super(conditionsIn);
            this.item = item;
            this.amount = amount;
            this.override = override;
        }

        private Modifier() {
            this(new LootItemCondition[] { }, Items.POTATO, 1, false);
        }

        @Override
        protected @NotNull ObjectArrayList<ItemStack> doApply(@NotNull ObjectArrayList<ItemStack> generatedLoot, @NotNull LootContext context) {
            if (ModGameRules.TECHNOPIGS_DROP_SPECIAL_LOOT.get(context.getLevel()) && TechnoPigMod.isTechnoPig(context.getParamOrNull(LootContextParams.THIS_ENTITY))) {
                if (override) generatedLoot.clear();
                generatedLoot.add(new ItemStack(item, amount));
            }

            return generatedLoot;
        }

        private static final class Serializer extends GlobalLootModifierSerializer<Modifier> {
            @Override
            public @NotNull Modifier read(ResourceLocation name, @NotNull JsonObject json, LootItemCondition[] conditionsIn) {
                Item item = Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(GsonHelper.getAsString(json, "item"))));
                int amount = GsonHelper.getAsInt(json, "amount");
                boolean override = GsonHelper.getAsBoolean(json, "override");
                return new Modifier(conditionsIn, item, amount, override);
            }

            @Override
            public @NotNull JsonObject write(@NotNull Modifier instance) {
                final JsonObject json = makeConditions(instance.conditions);
                json.addProperty("item", Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(instance.item)).toString());
                json.addProperty("amount", instance.amount);
                json.addProperty("override", instance.override);
                return json;
            }
        }
    }

    private static final class DataProvider extends GlobalLootModifierProvider {
        private DataProvider(@NotNull DataGenerator generator) {
            super(generator, TechnoPigMod.MOD_ID);
        }

        @Override
        protected void start() {
            add("technopig", serializer.get(), new Modifier());
        }
    }
}
