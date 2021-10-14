package lekavar.lma.drinkbeer.statuseffects;

import lekavar.lma.drinkbeer.DrinkBeer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.enchantment.FrostWalkerEnchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.awt.*;

public class DrunkFrostWalkerStatusEffect extends StatusEffect {
    public DrunkFrostWalkerStatusEffect() {
        super(StatusEffectType.BENEFICIAL, new Color(30, 144, 255, 255).getRGB());
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity instanceof PlayerEntity) {
            int remainingTime = entity.getStatusEffect(DrinkBeer.DRUNK_FROST_WALKER).getDuration();
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, remainingTime));
            FrostWalkerEnchantment.freezeWater(entity, entity.world, new BlockPos(entity.getPos()), 1);
        }
    }
}
