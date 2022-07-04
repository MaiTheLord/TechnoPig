package com.technopig.technopig;

import com.technopig.technopig.technopigcapability.TechnoPigCapability;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

@Mod(TechnoPigMod.MOD_ID)
public final class TechnoPigMod {
    public static final String MOD_ID = "technopig";

    public static boolean isTechnoPig(@Nullable Entity pig) {
        //TODO get date from the server and make it override this
        return TechnoPigCapability.isTechnoPig(pig);
    }
}
