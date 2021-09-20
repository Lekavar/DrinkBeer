package lekavar.lma.drinkbeer.item;

import lekavar.lma.drinkbeer.DrinkBeer;
import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class BeerMugBlockItem extends BlockItem {
        public BeerMugBlockItem(Block block,Item.Settings settings) {
            super(block,settings);
        }

        public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
            ItemStack itemStack = super.finishUsing(stack, world, user);
            if(user instanceof PlayerEntity && ((PlayerEntity)user).abilities.creativeMode){
                return itemStack;
            }
            else{
                ItemStack emptyMugItemStack = new ItemStack(DrinkBeer.EMPTY_BEER_MUG.asItem(),1);
                ((PlayerEntity)user).giveItemStack(emptyMugItemStack);
                return itemStack;
            }
        }

    @Override
    public SoundEvent getEatSound() {
        return DrinkBeer.DRINKING_BEER_EVENT;
    }
}
