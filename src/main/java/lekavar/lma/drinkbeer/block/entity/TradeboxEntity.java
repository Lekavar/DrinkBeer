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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;

public class TradeboxEntity extends BlockEntity implements ImplementedInventory, NamedScreenHandlerFactory, Tickable {
    private DefaultedList<ItemStack> goodInventory = DefaultedList.ofSize(8, ItemStack.EMPTY);
    private int coolingTime;
    private int locationId;
    private int residentId;
    private int process;
    public TradeBoxScreenHandler screenHandler;

    public static final int PROCESS_COOLING = 0;
    public static final int PROCESS_TRADING = 1;

    public TradeboxEntity() {
        super(DrinkBeer.TRADE_BOX_ENTITY);
    }

    public TradeboxEntity(int coolingTime) {
        super(DrinkBeer.TRADE_BOX_ENTITY);

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
        return new TranslatableText(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        this.screenHandler = new TradeBoxScreenHandler(syncId, playerInventory, this, this.pos, ScreenHandlerContext.create(world, pos), propertyDelegate);
        return this.screenHandler;
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        Inventories.toTag(tag, this.goodInventory);
        tag.putShort("CoolingTime", (short) this.coolingTime);
        tag.putShort("LocationId", (short) this.locationId);
        tag.putShort("ResidentId", (short) this.residentId);
        tag.putShort("Process", (short) this.process);

        return super.toTag(tag);
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        Inventories.fromTag(tag, goodInventory);
        this.coolingTime = tag.getShort("CoolingTime");
        this.locationId = tag.getShort("LocationId");
        this.residentId = tag.getShort("ResidentId");
        this.process = tag.getShort("Process");
    }

    @Override
    public void tick() {
        if (!world.isClient) {
            coolingTime = coolingTime > 0 ? --coolingTime : 0;
            if (coolingTime == 0 && propertyDelegate.get(3) == PROCESS_COOLING) {
                if (screenHandler != null) {
                    this.screenHandler.setTradeboxTrading();
                }
            }
        }
    }
}
