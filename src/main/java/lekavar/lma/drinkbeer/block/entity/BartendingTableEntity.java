package lekavar.lma.drinkbeer.block.entity;

import lekavar.lma.drinkbeer.DrinkBeer;
import lekavar.lma.drinkbeer.item.BeerMugBlockItem;
import lekavar.lma.drinkbeer.item.MixedBeerBlockItem;
import lekavar.lma.drinkbeer.manager.MixedBeerManager;
import lekavar.lma.drinkbeer.screen.BartendingTableScreenHandler;
import lekavar.lma.drinkbeer.util.beer.Beers;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.ArrayList;
import java.util.List;

public class BartendingTableEntity extends BlockEntity implements NamedScreenHandlerFactory {
    private int beerId = Beers.EMPTY_BEER_ID;
    private List<Integer> spiceList = new ArrayList<>();
    private int isMixedBeer = 0;

    public BartendingTableEntity() {
        super(DrinkBeer.BARTENDING_TABLE_ENTITY);
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText(getCachedState().getBlock().getTranslationKey());
    }


    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new BartendingTableScreenHandler(syncId, playerInventory, beerId, isMixedBeer, spiceList, ScreenHandlerContext.create(world, pos));
    }

    public boolean setBeer(ItemStack beerItemStack) {
        try {
            Item beerItem = beerItemStack.getItem();
            if (beerItem instanceof BeerMugBlockItem) {
                isMixedBeer = 0;
                //No spiceList for a basic liquor
                //Get beerId
                this.beerId = (Beers.byItem(beerItem).getId());
            } else if (beerItem instanceof MixedBeerBlockItem) {
                isMixedBeer = 1;
                //Read beerId
                this.beerId = (MixedBeerManager.getBeerId(beerItemStack));
                //Read spiceList
                this.spiceList = MixedBeerManager.getSpiceList(beerItemStack);
            }
            return true;
        } catch (Exception e) {
            System.out.println("Something wrong when reading beer properties in BartendingTableEntity");
            return false;
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        //Write beerId
        tag.putShort("beerId", (short) this.beerId);
        //Write isMixedBeer
        tag.putShort("isMixedBeer", (short) this.isMixedBeer);
        //Write spiceList
        ListTag listTag = MixedBeerManager.genSpiceListTag(spiceList);
        if (listTag != null) {
            tag.put("Spices", listTag);
        }

        return super.toTag(tag);
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);

        //Read beerId
        this.beerId = tag.getShort("beerId");
        //Read isMixedBeer
        this.isMixedBeer = tag.getShort("isMixedBeer");
        //Read spiceList
        this.spiceList.clear();
        ListTag listTag = tag.getList("Spices", 10);
        for (int i = 0; i < listTag.size() && i < MixedBeerManager.MAX_SPICES_COUNT; ++i) {
            CompoundTag tag2 = listTag.getCompound(i);
            int spiceId = tag2.getInt("SpiceId");
            this.spiceList.add(spiceId);
        }
    }
}
