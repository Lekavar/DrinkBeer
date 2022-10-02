package lekavar.lma.drinkbeer.event;

import lekavar.lma.drinkbeer.DrinkBeer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

public class PlayerWakeUpEvent {
    public static void onStopSleeping(LivingEntity livingEntity, BlockPos blockPos) {
        if (livingEntity instanceof ServerPlayerEntity player &&
            player.getWorld().getTimeOfDay() % 24000 == 0) {
            player.removeStatusEffect(DrinkBeer.DRUNK);
        }
    }
}
