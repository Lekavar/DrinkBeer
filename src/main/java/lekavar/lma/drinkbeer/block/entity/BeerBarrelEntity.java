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

    public static void tick(World world, BlockPos pos, BlockState state, BeerBarrelEntity beerBarrelEntity) {
        if (!world.isClient)
            beerBarrelEntity.remainingBrewingTime = beerBarrelEntity.remainingBrewingTime > 0 ? --beerBarrelEntity.remainingBrewingTime : 0;
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
        tag.putShort("RemainingBrewTime", (short)this.remainingBrewingTime);
        tag.putShort("IsMaterialCompleted", (short)this.isMaterialCompleted);
        tag.putShort("BeerType", (short)this.beerId);
        tag.putShort("IsBrewing", (short)this.isBrewing);
        tag.putShort("BeerResultNum", (short)this.beerResultNum);

        return tag;
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        Inventories.readNbt(tag, inventory);
        this.remainingBrewingTime = tag.getShort("RemainingBrewTime");
        this.isMaterialCompleted = tag.getShort("IsMaterialCompleted");
        this.beerId = tag.getShort("BeerType");
        this.isBrewing = tag.getShort("IsBrewing");
        this.beerResultNum = tag.getShort("BeerResultNum");
    }
}
