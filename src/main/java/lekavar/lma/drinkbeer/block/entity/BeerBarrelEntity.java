package lekavar.lma.drinkbeer.block.entity;

import lekavar.lma.drinkbeer.DrinkBeer;
import lekavar.lma.drinkbeer.ImplementedInventory;
import lekavar.lma.drinkbeer.screen.BeerBarrelScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BeerBarrelEntity extends BlockEntity implements ImplementedInventory, NamedScreenHandlerFactory{
    private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(6, ItemStack.EMPTY);
    private int remainingBrewTime;
    private int isMaterialCompleted;
    private int beerType;
    private int isBrewing;

    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    private final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            switch(index) {
                case 0:
                    return remainingBrewTime;
                case 1:
                    return isMaterialCompleted;
                case 2:
                    return beerType;
                case 3:
                    return isBrewing;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch(index) {
                case 0:
                    remainingBrewTime = value;
                    break;
                case 1:
                    isMaterialCompleted = value;
                    break;
                case 2:
                    beerType = value;
                    break;
                case 3:
                    isBrewing = value;
            }
        }

        @Override
        public int size() {
            return 4;
        }
    };

    public static void tick(World world, BlockPos pos, BlockState state, BeerBarrelEntity beerBarrelEntity) {
        if (!world.isClient)
            beerBarrelEntity.remainingBrewTime = beerBarrelEntity.remainingBrewTime > 0 ? --beerBarrelEntity.remainingBrewTime : 0;
    }

    public BeerBarrelEntity(BlockPos pos,BlockState state) {
        super(DrinkBeer.INSTANCE.BEER_BARREL_ENTITY,pos,state);
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
    public NbtCompound writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        Inventories.writeNbt(tag,inventory);
        tag.putShort("RemainingBrewTime", (short)this.remainingBrewTime);
        tag.putShort("IsMaterialCompleted", (short)this.isMaterialCompleted);
        tag.putShort("BeerType", (short)this.beerType);
        tag.putShort("IsBrewing", (short)this.isBrewing);

        return tag;
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        Inventories.readNbt(tag, inventory);
        this.remainingBrewTime = tag.getShort("RemainingBrewTime");
        this.isMaterialCompleted = tag.getShort("IsMaterialCompleted");
        this.beerType = tag.getShort("BeerType");
        this.isBrewing = tag.getShort("IsBrewing");
    }
}
