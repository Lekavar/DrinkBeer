package lekavar.lma.drinkbeer.item;

import lekavar.lma.drinkbeer.DrinkBeer;
import lekavar.lma.drinkbeer.block.MixedBeerBlock;
import lekavar.lma.drinkbeer.manager.MixedBeerManager;
import lekavar.lma.drinkbeer.manager.SpiceAndFlavorManager;
import lekavar.lma.drinkbeer.util.beer.Beers;
import lekavar.lma.drinkbeer.util.mixedbeer.Flavors;
import lekavar.lma.drinkbeer.util.mixedbeer.Spices;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
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

public class MixedBeerBlockItem extends BeerBlockItem {
    public MixedBeerBlockItem() {
        super(DrinkBeer.MIXED_BEER, new Item.Settings().group(DrinkBeer.DRINK_BEER).maxCount(1)
                .food(new FoodComponent.Builder().alwaysEdible().build()));
    }

    @Environment(EnvType.CLIENT)
    public void appendMixedBeerTooltip(ItemStack stack, List<Text> tooltip) {
        //Base title
        tooltip.add(new TranslatableText(MixedBeerManager.getBaseBeerToolTipTranslationKey()).append(":").formatted(Formatting.WHITE));
        //Base beer
        int beerId = MixedBeerManager.getBeerId(stack);
        Item beerItem = Beers.byId(beerId).getBeerItem();
        String beerName = beerId > Beers.EMPTY_BEER_ID ? beerItem.getTranslationKey()
                : MixedBeerManager.getUnmixedToolTipTranslationKey();
        tooltip.add(new TranslatableText(beerName).formatted(Formatting.BLUE));
        //Base status effect tooltip
        if (beerId > Beers.EMPTY_BEER_ID) {
            String name = beerItem.asItem().toString();
            if (Beers.byId(beerId).getHasStatusEffectTooltip()) {
                tooltip.add(new TranslatableText("item.drinkbeer." + name + ".tooltip").formatted(Formatting.BLUE));
            }
        }
        //Base food level
        if (beerId > Beers.EMPTY_BEER_ID) {
            Text hunger = Text.of(String.valueOf(beerItem.getFoodComponent().getHunger()));
            tooltip.add(new TranslatableText("drinkbeer.restores_hunger").formatted(Formatting.BLUE).append(hunger));
        }

        //Flavor title
        tooltip.add(new TranslatableText(SpiceAndFlavorManager.getFlavorToolTipTranslationKey()).append(":").formatted(Formatting.WHITE));
        //Flavor
        List<Integer> spiceList = MixedBeerManager.getSpiceList(stack);
        if (!spiceList.isEmpty()) {
            for (int spiceId : spiceList) {
                Flavors flavor = Spices.byId(spiceId).getFlavor();
                tooltip.add(new TranslatableText(SpiceAndFlavorManager.getFlavorTranslationKey(flavor.getId()))
                        .append("(")
                        .append(new TranslatableText(SpiceAndFlavorManager.getFlavorToolTipTranslationKey(flavor.getId())))
                        .append(")")
                        .formatted(Formatting.RED));
            }
        } else {
            tooltip.add(new TranslatableText(SpiceAndFlavorManager.getNoFlavorToolTipTranslationKey()).formatted(Formatting.RED));
        }
        //Flavor combination(if exists)
        Flavors combinedFlavor = SpiceAndFlavorManager.getCombinedFlavor(spiceList);
        if (combinedFlavor != null) {
            tooltip.add(new TranslatableText("")
                    .append("\"")
                    .append(new TranslatableText(SpiceAndFlavorManager.getFlavorTranslationKey(combinedFlavor.getId())))
                    .append("\"")
                    .formatted(Formatting.DARK_RED));
        }
    }

    @Override
    public Text getName(ItemStack stack) {
        return getMixedBeerName(stack);
    }

    @Environment(EnvType.CLIENT)
    public Text getMixedBeerName(ItemStack stack) {
        int beerId = MixedBeerManager.getBeerId(stack);
        Item beerItem = Beers.byId(beerId).getBeerItem();
        String beerName = beerId > Beers.EMPTY_BEER_ID ? beerItem.getTranslationKey() : "block.drinkbeer.empty_beer_mug";
        Text name = new TranslatableText(beerName).append(new TranslatableText(MixedBeerManager.getMixedBeerTranslationKey())).formatted(Formatting.YELLOW);
        return name;
    }

    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        appendMixedBeerTooltip(stack, tooltip);
    }

    @Override
    protected boolean place(ItemPlacementContext context, BlockState state) {
        ItemStack stack = context.getStack();
        ((MixedBeerBlock) state.getBlock()).setBeerId(MixedBeerManager.getBeerId(stack));
        ((MixedBeerBlock) state.getBlock()).setSpiceList(MixedBeerManager.getSpiceList(stack));
        return super.place(context, state);
    }

    @Override
    protected boolean canPlace(ItemPlacementContext context, BlockState state) {
        if (getDistance(context.getHitPos(), context.getPlayer().getPos()) > MAX_PLACE_DISTANCE)
            return false;
        else {
            return super.canPlace(context, state);
        }
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        //Apply mixed beer
        if(!world.isClient()) {
            MixedBeerManager.useMixedBeer(stack, world, user);
        }
        //Give empty mug back
        giveEmptyMugBack(user);

        return super.finishUsing(stack, world, user);
    }
}
