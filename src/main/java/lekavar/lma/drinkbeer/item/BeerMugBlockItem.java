package lekavar.lma.drinkbeer.item;

import lekavar.lma.drinkbeer.DrinkBeer;
import lekavar.lma.drinkbeer.statuseffects.DrunkStatusEffect;
import lekavar.lma.drinkbeer.statuseffects.NightHowlStatusEffect;
import lekavar.lma.drinkbeer.util.beer.Beers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BeerMugBlockItem extends BeerBlockItem {
    public BeerMugBlockItem(Block block, @Nullable StatusEffectInstance statusEffectInstance, int hunger) {
        super(block, new Item.Settings().group(DrinkBeer.DRINK_BEER).maxCount(16)
                .food(statusEffectInstance != null
                        ? new FoodComponent.Builder().hunger(hunger).statusEffect(statusEffectInstance, 1).alwaysEdible().build()
                        : new FoodComponent.Builder().hunger(hunger).alwaysEdible().build())
        );
    }

    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        //Give Drunk status effect
        DrunkStatusEffect.addStatusEffect(user);
        //Give Night Vision status effect if drank Night Howl Kvass
        NightHowlStatusEffect.addStatusEffect(stack,world,user);
        //Give empty mug back
        giveEmptyMugBack(user);

        return super.finishUsing(stack, world, user);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        String name = this.asItem().toString();
        if (Beers.byItem(this.asItem()).getHasStatusEffectTooltip()) {
            tooltip.add(new TranslatableText("item.drinkbeer." + name + ".tooltip").formatted(Formatting.BLUE));
        }
        Text hunger = Text.of(String.valueOf(asItem().getFoodComponent().getHunger()));
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
}