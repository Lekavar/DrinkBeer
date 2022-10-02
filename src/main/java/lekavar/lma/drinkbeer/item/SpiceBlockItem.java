package lekavar.lma.drinkbeer.item;

import lekavar.lma.drinkbeer.DrinkBeer;
import lekavar.lma.drinkbeer.manager.SpiceAndFlavorManager;
import lekavar.lma.drinkbeer.util.mixedbeer.Flavors;
import lekavar.lma.drinkbeer.util.mixedbeer.Spices;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.BlockItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpiceBlockItem extends BlockItem {
    public SpiceBlockItem(Block block, @Nullable StatusEffectInstance statusEffectInstance, int hunger) {
        super(block, new Item.Settings().group(DrinkBeer.DRINK_BEER_GENERAL).maxCount(64)
                .food(statusEffectInstance != null
                        ? new FoodComponent.Builder().hunger(hunger).statusEffect(statusEffectInstance, 1).alwaysEdible().build()
                        : new FoodComponent.Builder().hunger(hunger).alwaysEdible().build())
        );
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        //Spice title
        tooltip.add(Text.translatable(SpiceAndFlavorManager.getSpiceToolTipTranslationKey()).formatted(Formatting.YELLOW));
        //Flavor title
        tooltip.add(Text.translatable(SpiceAndFlavorManager.getFlavorToolTipTranslationKey()).append(":").formatted(Formatting.WHITE));
        //Flavor and tooltip
        Flavors flavor = Spices.byItem(this.asItem()).getFlavor();
        tooltip.add(Text.translatable(SpiceAndFlavorManager.getFlavorTranslationKey(flavor.getId()))
                .append("(")
                .append(Text.translatable(SpiceAndFlavorManager.getFlavorToolTipTranslationKey(flavor.getId())))
                .append(")")
                .formatted(Formatting.RED));
    }
}
