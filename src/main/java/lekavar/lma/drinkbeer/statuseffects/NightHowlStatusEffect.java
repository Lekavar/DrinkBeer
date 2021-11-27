package lekavar.lma.drinkbeer.statuseffects;

import lekavar.lma.drinkbeer.DrinkBeer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Random;

public class NightHowlStatusEffect {
    private final static int BASE_NIGHT_VISION_TIME = 2400;

    public static void addStatusEffect(ItemStack stack, World world, LivingEntity user) {
        if (stack.getItem() == DrinkBeer.BEER_MUG_NIGHT_HOWL_KVASS.asItem()) {
            //Duration will be longest when the moon is full, shortest when new
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, getNightVisionTime(getMoonPhase(world))));
            //Play random howl sound
            playRandomHowlSound(world, user);
        }
    }

    public static org.apache.commons.lang3.tuple.Pair<StatusEffect, Integer> getStatusEffectPair(World world) {
        return Pair.of(StatusEffects.NIGHT_VISION, getNightVisionTime(getMoonPhase(world)));
    }

    public static void playRandomHowlSound(World world, LivingEntity user) {
        if (!world.isClient) {
            world.playSound(null, user.getBlockPos(), DrinkBeer.NIGHT_HOWL_EVENT[new Random().nextInt(4)], SoundCategory.PLAYERS, 1.2f, 1f);
        }
    }

    private static int getNightVisionTime(int moonPhase) {
        return BASE_NIGHT_VISION_TIME + (moonPhase == 0 ? Math.abs(moonPhase - 1 - 4) * 1200 : Math.abs(moonPhase - 4) * 1200);
    }

    private static int getMoonPhase(World world) {
        try {
            long timeOfDay = world.getLevelProperties().getTimeOfDay();
            return (int) (timeOfDay / 24000L % 8L + 8L) % 8;
        } catch (Exception e) {
            return 0;
        }
    }
}
