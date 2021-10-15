package lekavar.lma.drinkbeer.item;

import lekavar.lma.drinkbeer.DrinkBeer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static java.lang.Math.pow;
import static net.minecraft.util.math.MathHelper.sqrt;

public class BeerMugBlockItem extends BlockItem {
    private final static float MAX_PLACE_DISTANCE = (float) 2;

    public BeerMugBlockItem(Block block, Item.Settings settings) {
        super(block, settings);
    }

    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        ItemStack itemStack = super.finishUsing(stack, world, user);
        if (user instanceof PlayerEntity && ((PlayerEntity) user).abilities.creativeMode) {
            return itemStack;
        } else {
            ItemStack emptyMugItemStack = new ItemStack(DrinkBeer.EMPTY_BEER_MUG.asItem(), 1);
            ((PlayerEntity) user).giveItemStack(emptyMugItemStack);
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
        if (this.asItem() != DrinkBeer.BEER_MUG_PUMPKIN_KVASS.asItem()) {
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
}
