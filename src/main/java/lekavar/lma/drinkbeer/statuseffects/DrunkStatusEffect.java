package lekavar.lma.drinkbeer.statuseffects;

import lekavar.lma.drinkbeer.DrinkBeer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

import java.awt.*;

public class DrunkStatusEffect extends StatusEffect {
    public final static int MAX_DRUNK_AMPLIFIER = 4;
    public final static int MIN_DRUNK_AMPLIFIER = 0;
    private final static int BASE_DURATION = 1200;
    private final static boolean visible = false;
    private static final int[] drunkDurations = {3600, 3000, 2400, 1800, 1200};
    private static final int[] nauseaDurations = {160, 160, 200, 300, 600};
    private static final int[] slownessDurations = {0, 80, 160, 200, 600};
    private static final int[] harmulStatusEffectsIntervals = {200, 160, 200, 300, 20};

    public DrunkStatusEffect() {
        super(StatusEffectCategory.HARMFUL, new Color(255, 222, 173, 255).getRGB());
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    //Value: > 0:Increase drunk amplifier 0 <:Decrease drunk amplifier
    public static void addStatusEffect(LivingEntity user, int value) {
        if (value == 0) {
            return;
        }

        StatusEffectInstance statusEffectInstance = user.getStatusEffect(DrinkBeer.DRUNK);
        int currentDrunkAmplifier = statusEffectInstance == null ? -1 : statusEffectInstance.getAmplifier();
        int newDrunkAmplifier = currentDrunkAmplifier + value;
        newDrunkAmplifier = Math.min(newDrunkAmplifier, MAX_DRUNK_AMPLIFIER);

        if (currentDrunkAmplifier < MIN_DRUNK_AMPLIFIER && newDrunkAmplifier < MIN_DRUNK_AMPLIFIER) {
            return;
        } else if (currentDrunkAmplifier >= MIN_DRUNK_AMPLIFIER && newDrunkAmplifier < MIN_DRUNK_AMPLIFIER) {
            user.removeStatusEffect(DrinkBeer.DRUNK);
        } else if (currentDrunkAmplifier < MIN_DRUNK_AMPLIFIER) {
            user.addStatusEffect(new StatusEffectInstance(DrinkBeer.DRUNK, DrunkStatusEffect.getDrunkDuratioin(newDrunkAmplifier), newDrunkAmplifier));
        } else {
            if(newDrunkAmplifier > currentDrunkAmplifier){
                user.addStatusEffect(new StatusEffectInstance(DrinkBeer.DRUNK, DrunkStatusEffect.getDrunkDuratioin(newDrunkAmplifier), newDrunkAmplifier));
            }
            else if(newDrunkAmplifier < currentDrunkAmplifier){
                int tempDrunkAmplifier = currentDrunkAmplifier - newDrunkAmplifier;
                while(tempDrunkAmplifier > 0){
                    decreaseDrunkStatusEffefct(user,currentDrunkAmplifier);
                    currentDrunkAmplifier--;
                    tempDrunkAmplifier --;
                }
            }
        }
    }

    public static void addStatusEffect(LivingEntity user) {
        addStatusEffect(user,1);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        int time = entity.getStatusEffect(DrinkBeer.DRUNK).getDuration();
        //Always give harmful status effects
        giveHarmfulStatusEffects(entity, amplifier, time);
        //Give next lower Drunk status effect when duration's out
        if (time == 1) {
            decreaseDrunkStatusEffefct(entity, amplifier);
        }
    }

    private void giveHarmfulStatusEffects(LivingEntity entity, int amplifier, int time) {
        if (amplifier >= MAX_DRUNK_AMPLIFIER) {
            int duration = entity.getStatusEffect(DrinkBeer.DRUNK).getDuration();
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, duration, 0, false, visible));
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, duration, MAX_DRUNK_AMPLIFIER - 1, false, visible));
        } else if (time % harmulStatusEffectsIntervals[amplifier] == 0) {
            int nauseaDuration = nauseaDurations[amplifier];
            int slownessDuration = slownessDurations[amplifier];
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, nauseaDuration, 0, false, visible));
            if (amplifier > 0) {
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, slownessDuration, amplifier - 1, false, visible));
            }
        }
    }

    private static void decreaseDrunkStatusEffefct(LivingEntity entity, int amplifier) {
        if (!entity.getEntityWorld().isClient()) {
            entity.removeStatusEffect(DrinkBeer.DRUNK);
            StatusEffectInstance nextDrunkStatusEffect = getDecreasedDrunkStatusEffect(amplifier);
            if (nextDrunkStatusEffect != null) {
                entity.addStatusEffect(nextDrunkStatusEffect);
            }
        }
    }

    private static StatusEffectInstance getDecreasedDrunkStatusEffect(int currentAmplifier) {
        int nextDrunkAmplifier = currentAmplifier - 1;
        if (nextDrunkAmplifier < MIN_DRUNK_AMPLIFIER) {
            return null;
        } else {
            return new StatusEffectInstance(DrinkBeer.DRUNK, getDrunkDuratioin(nextDrunkAmplifier), nextDrunkAmplifier);
        }
    }

    public static int getNextDrunkAmplifier(LivingEntity user) {
        StatusEffectInstance statusEffectInstance = user.getStatusEffect(DrinkBeer.DRUNK);
        int drunkAmplifier = statusEffectInstance == null ? -1 : statusEffectInstance.getAmplifier();
        return drunkAmplifier < MAX_DRUNK_AMPLIFIER ? drunkAmplifier + 1 : drunkAmplifier;
    }

    public static int getDrunkDuratioin(int amplifier) {
        try {
            return drunkDurations[amplifier];
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Amplifier is out of range");
            return BASE_DURATION;
        }
    }
}
