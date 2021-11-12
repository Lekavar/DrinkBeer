package lekavar.lma.drinkbeer.item;

import lekavar.lma.drinkbeer.DrinkBeer;
import lekavar.lma.drinkbeer.statuseffects.DrunkStatusEffect;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

import static java.lang.Math.pow;
import static net.minecraft.util.math.MathHelper.sqrt;

public class BeerMugBlockItem extends BlockItem {
    private final static float MAX_PLACE_DISTANCE = (float) 2;
    private final static int BASE_NIGHT_VISION_TIME = 2400;
    private final boolean hasStatusEffectTooltip;

    public BeerMugBlockItem(Block block, @Nullable StatusEffectInstance statusEffectInstance, int hunger, boolean hasStatusEffectTooltip) {
        super(block, new Item.Settings().group(DrinkBeer.DRINK_BEER).maxCount(16)
                .food(statusEffectInstance != null
                        ? new FoodComponent.Builder().hunger(hunger).statusEffect(statusEffectInstance, 1).alwaysEdible().build()
                        : new FoodComponent.Builder().hunger(hunger).alwaysEdible().build())
        );
        this.hasStatusEffectTooltip = hasStatusEffectTooltip;
    }

    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        ItemStack itemStack = super.finishUsing(stack, world, user);
        //Give Drunk status effect
        int drunkAmplifier = DrunkStatusEffect.getDrunkAmplifier(user);
        user.addStatusEffect(new StatusEffectInstance(DrinkBeer.DRUNK, DrunkStatusEffect.getDrunkDuratioin(drunkAmplifier), drunkAmplifier));
        //Give Night Vision status effect after drinking Night Howl Kvass
        //Duration is longest when the moon is full, shortest when new
        if (stack.getItem() == DrinkBeer.BEER_MUG_NIGHT_HOWL_KVASS.asItem()) {
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, getNightVisionTime(getMoonPhase(world))));
            if (!world.isClient) {
                world.playSound(null, user.getBlockPos(), DrinkBeer.NIGHT_HOWL_EVENT[new Random().nextInt(4)], SoundCategory.PLAYERS, 1.2f, 1f);
            }
        }
        //Give empty mug back
        if (user instanceof PlayerEntity && ((PlayerEntity) user).abilities.creativeMode) {
            return itemStack;
        } else {
            ItemStack emptyMugItemStack = new ItemStack(DrinkBeer.EMPTY_BEER_MUG.asItem(), 1);
            if (user instanceof PlayerEntity) {
                if (!((PlayerEntity) user).giveItemStack(emptyMugItemStack))
                    ((PlayerEntity) user).dropItem(emptyMugItemStack, false);
            } else {
                user.dropStack(emptyMugItemStack);
            }
            return itemStack;
        }
    }

    @Override
    public SoundEvent getEatSound() {
        return DrinkBeer.DRINKING_BEER_EVENT;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        String name = this.asItem().toString();
        if (this.hasStatusEffectTooltip) {
            tooltip.add(new TranslatableText("item.drinkbeer." + name + ".tooltip").formatted(Formatting.BLUE));
        }
        Text hunger = Text.method_30163(String.valueOf(asItem().getFoodComponent().getHunger()));
        tooltip.add(new TranslatableText("drinkbeer.restores_hunger").formatted(Formatting.BLUE).append(hunger));
    }

    @Override
    protected boolean canPlace(ItemPlacementContext context, BlockState state) {
        if (getDistance(context.getHitPos(), context.getPlayer().getPos()) > MAX_PLACE_DISTANCE)
            return false;
        else {
            return super.canPlace(context, state);
        }
    }

    private float getDistance(Vec3d p1, Vec3d p2) {
        return sqrt(pow(p1.x - p2.x, 2) + pow(p1.y - p2.y, 2) + pow(p1.z - p2.z, 2));
    }

    private int getNightVisionTime(int moonPhase) {
        return BASE_NIGHT_VISION_TIME + (moonPhase == 0 ? Math.abs(moonPhase - 1 - 4) * 1200 : Math.abs(moonPhase - 4) * 1200);
    }

    private int getMoonPhase(World world) {
        long timeOfDay = world.getLevelProperties().getTimeOfDay();
        int moonPhase = (int) (timeOfDay / 24000L % 8L + 8L) % 8;
        return moonPhase;
    }
}
