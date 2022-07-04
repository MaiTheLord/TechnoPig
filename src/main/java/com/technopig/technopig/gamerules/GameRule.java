package com.technopig.technopig.gamerules;

import com.technopig.technopig.TechnoPigMod;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class GameRule<T, V extends GameRules.Value<V>> {
    private final String name;
    private final GameRules.Category category;
    private final GameRules.Type<V> type;
    private final String displayName;

    protected GameRules.Key<V> key;

    private GameRule(@NotNull String name, @NotNull GameRules.Category category, @NotNull GameRules.Type<V> type, @NotNull String displayName) {
        this.name = name;
        this.category = category;
        this.type = type;
        this.displayName = displayName;
    }

    void register() {
        this.key = GameRules.register(name, category, type);
    }

    public abstract @NotNull T get(@NotNull Level level);

    public static final class Boolean extends GameRule<java.lang.Boolean, GameRules.BooleanValue> {
        Boolean(@NotNull String name, boolean defaultValue, @NotNull String displayName, @NotNull GameRules.Category category) {
            super(name, category, GameRules.BooleanValue.create(defaultValue), displayName);
        }

        @Override
        public @NotNull java.lang.Boolean get(@NotNull Level level) {
            return level.getGameRules().getBoolean(key);
        }
    }

    public static final class Integer extends GameRule<java.lang.Integer, GameRules.IntegerValue> {
        Integer(@NotNull String name, int defaultValue, @NotNull String displayName, @NotNull GameRules.Category category) {
            super(name, category, GameRules.IntegerValue.create(defaultValue), displayName);
        }

        @Override
        public @NotNull java.lang.Integer get(@NotNull Level level) {
            return level.getGameRules().getInt(key);
        }
    }

    static class LanguageProvider extends net.minecraftforge.common.data.LanguageProvider {
        private final List<GameRule<?, ?>> gameRules;

        LanguageProvider(@NotNull DataGenerator generator, @NotNull List<GameRule<?, ?>> gameRules) {
            super(generator, TechnoPigMod.MOD_ID, "en_us");
            this.gameRules = gameRules;
        }

        @Override
        protected void addTranslations() {
            gameRules.forEach(gameRule -> add("gamerule.%s".formatted(gameRule.name), gameRule.displayName));
        }
    }
}
