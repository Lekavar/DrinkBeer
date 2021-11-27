package lekavar.lma.drinkbeer.item;

import lekavar.lma.drinkbeer.DrinkBeer;
import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.Vec3d;

import static java.lang.Math.pow;
import static net.minecraft.util.math.MathHelper.sqrt;

public class BeerBlockItem extends BlockItem {
    protected final static float MAX_PLACE_DISTANCE = (float) 2;

    public BeerBlockItem(Block block, Item.Settings settings) {
        super(block, settings);
    }

    @Override
    public SoundEvent getEatSound() {
        return DrinkBeer.DRINKING_BEER_EVENT;
    }

    public float getDistance(Vec3d p1, Vec3d p2) {
        return sqrt((float) (pow(p1.x - p2.x, 2) + pow(p1.y - p2.y, 2) + pow(p1.z - p2.z, 2)));
    }

    public void giveEmptyMugBack(LivingEntity user) {
        if (!(user instanceof PlayerEntity) || !((PlayerEntity) user).isCreative()) {
            ItemStack emptyMugItemStack = new ItemStack(DrinkBeer.EMPTY_BEER_MUG.asItem(), 1);
            if (user instanceof PlayerEntity) {
                if (!((PlayerEntity) user).giveItemStack(emptyMugItemStack))
                    ((PlayerEntity) user).dropItem(emptyMugItemStack, false);
            } else {
                user.dropStack(emptyMugItemStack);
            }
        }
    }
}
