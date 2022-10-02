package lekavar.lma.drinkbeer.block.entity;

import lekavar.lma.drinkbeer.DrinkBeer;
import lekavar.lma.drinkbeer.inventory.ImplementedInventory;
import lekavar.lma.drinkbeer.manager.TradeboxManager;
import lekavar.lma.drinkbeer.screen.TradeBoxScreenHandler;
import lekavar.lma.drinkbeer.util.tradebox.Locations;
import lekavar.lma.drinkbeer.util.tradebox.Residents;
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
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TradeboxEntity extends BlockEntity implements ImplementedInventory, NamedScreenHandlerFactory {
    private DefaultedList<ItemStack> goodInventory = DefaultedList.ofSize(8, ItemStack.EMPTY);
    private int coolingTime;
    private int locationId;
    private int residentId;
    private int process;
    public TradeBoxScreenHandler screenHandler;

    public static final int PROCESS_COOLING = 0;
    public static final int PROCESS_TRADING = 1;

    public TradeboxEntity(BlockPos pos, BlockState state) {
        super(DrinkBeer.INSTANCE.TRADE_BOX_ENTITY,pos,state);
    }

    public TradeboxEntity(int coolingTime,BlockPos pos, BlockState state) {
        super(DrinkBeer.INSTANCE.TRADE_BOX_ENTITY,pos,state);

        this.coolingTime = TradeboxManager.COOLING_TIME_ON_PLACE;
        this.locationId = Locations.EMPTY_LOCATION.getId();
        this.residentId = Residents.EMPTY_RESIDENT.getId();
        this.process = PROCESS_COOLING;

        propertyDelegate.set(0, coolingTime);
        propertyDelegate.set(1, locationId);
        propertyDelegate.set(2, residentId);
        propertyDelegate.set(3, process);

        this.markDirty();
    }

    public DefaultedList<ItemStack> getItems() {
        return goodInventory;
    }

    private final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return coolingTime;
                case 1:
                    return locationId;
                case 2:
                    return residentId;
                case 3:
                    return process;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    coolingTime = value;
                    break;
                case 1:
                    locationId = value;
                    break;
                case 2:
                    residentId = value;
                    break;
                case 3:
                    process = value;
                    break;
            }
        }

        @Override
        public int size() {
            return 4;
        }
    };

    @Override
    public Text getDisplayName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        this.screenHandler = new TradeBoxScreenHandler(syncId, playerInventory, this, this.pos, ScreenHandlerContext.create(world, pos), propertyDelegate);
        return this.screenHandler;
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        Inventories.writeNbt(tag, this.goodInventory);
        tag.putShort("CoolingTime", (short) this.coolingTime);
        tag.putShort("LocationId", (short) this.locationId);
        tag.putShort("ResidentId", (short) this.residentId);
        tag.putShort("Process", (short) this.process);

        super.writeNbt(tag);
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        Inventories.readNbt(tag, goodInventory);
        this.coolingTime = tag.getShort("CoolingTime");
        this.locationId = tag.getShort("LocationId");
        this.residentId = tag.getShort("ResidentId");
        this.process = tag.getShort("Process");
    }

    public static void tick(World world, BlockPos pos, BlockState state, TradeboxEntity tradeboxEntity) {
        if (!world.isClient) {
            tradeboxEntity.coolingTime = tradeboxEntity.coolingTime > 0 ? --tradeboxEntity.coolingTime : 0;
            if (tradeboxEntity.coolingTime == 0 && tradeboxEntity.propertyDelegate.get(3) == PROCESS_COOLING) {
                if (tradeboxEntity.screenHandler != null) {
                    tradeboxEntity.screenHandler.setTradeboxTrading();
                }
            }
        }
    }
}
