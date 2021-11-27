package lekavar.lma.drinkbeer.block.entity;

import lekavar.lma.drinkbeer.DrinkBeer;
import lekavar.lma.drinkbeer.inventory.ImplementedInventory;
import lekavar.lma.drinkbeer.screen.BeerBarrelScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;

public class BeerBarrelEntity extends BlockEntity implements ImplementedInventory, NamedScreenHandlerFactory, Tickable {
    private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(6, ItemStack.EMPTY);
    private int remainingBrewingTime;
    private int isMaterialCompleted;
    private int beerId;
    private int isBrewing;
    private int beerResultNum;

    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    private final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> remainingBrewingTime;
                case 1 -> isMaterialCompleted;
                case 2 -> beerId;
                case 3 -> isBrewing;
                case 4 -> beerResultNum;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> remainingBrewingTime = value;
                case 1 -> isMaterialCompleted = value;
                case 2 -> beerId = value;
                case 3 -> isBrewing = value;
                case 4 -> beerResultNum = value;
            }
        }

        @Override
        public int size() {
            return 5;
        }
    };

    @Override
    public void tick() {
        if (!world.isClient)
            remainingBrewingTime = remainingBrewingTime > 0 ? --remainingBrewingTime : 0;
    }

    public BeerBarrelEntity() {
        super(DrinkBeer.BEER_BARREL_ENTITY);
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new BeerBarrelScreenHandler(syncId, playerInventory, ScreenHandlerContext.create(world, pos), propertyDelegate);
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        Inventories.toTag(tag, this.inventory);
        tag.putShort("RemainingBrewTime", (short)this.remainingBrewingTime);
        tag.putShort("IsMaterialCompleted", (short)this.isMaterialCompleted);
        tag.putShort("BeerType", (short)this.beerId);
        tag.putShort("IsBrewing", (short)this.isBrewing);
        tag.putShort("BeerResultNum", (short)this.beerResultNum);

        return super.toTag(tag);
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        Inventories.fromTag(tag, inventory);
        this.remainingBrewingTime = tag.getShort("RemainingBrewTime");
        this.isMaterialCompleted = tag.getShort("IsMaterialCompleted");
        this.beerId = tag.getShort("BeerType");
        this.isBrewing = tag.getShort("IsBrewing");
        this.beerResultNum = tag.getShort("BeerResultNum");
    }
}
