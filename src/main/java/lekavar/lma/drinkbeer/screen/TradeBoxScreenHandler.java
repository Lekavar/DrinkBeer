package lekavar.lma.drinkbeer.screen;

import lekavar.lma.drinkbeer.DrinkBeer;
import lekavar.lma.drinkbeer.block.entity.TradeboxEntity;
import lekavar.lma.drinkbeer.manager.TradeboxManager;
import lekavar.lma.drinkbeer.util.tradebox.Good;
import lekavar.lma.drinkbeer.util.tradebox.Locations;
import lekavar.lma.drinkbeer.util.tradebox.Residents;
import lekavar.lma.drinkbeer.util.tradebox.TradeMission;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class TradeBoxScreenHandler extends ScreenHandler {
    private final Inventory tradeboxInventory;
    private final List<Slot> tradeboxSlots;
    /**
     * goodSlots:
     * 0-3 goods to loacation
     * 4-7 goods from location
     */
    private final List<Slot> goodSlots;
    PropertyDelegate propertyDelegate;

    private final ScreenHandlerContext context;
    private Runnable inventoryChangeListener;
    public PlayerEntity player;
    BlockPos pos;

    public TradeBoxScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public TradeBoxScreenHandler(int syncId, PlayerInventory playerInventory, final ScreenHandlerContext context) {
        this(syncId, playerInventory, new SimpleInventory(8), null, ScreenHandlerContext.EMPTY, new ArrayPropertyDelegate(4));
    }

    public TradeBoxScreenHandler(int syncId, PlayerInventory playerInventory, Inventory goodInventory, BlockPos pos, final ScreenHandlerContext context, PropertyDelegate propertyDelegate) {
        super(DrinkBeer.TRADE_BOX_SCREEN_HANDLER, syncId);

        this.propertyDelegate = propertyDelegate;
        this.addProperties(propertyDelegate);
        this.inventoryChangeListener = () -> {
        };
        this.context = context;
        this.player = playerInventory.player;
        this.pos = pos;

        tradeboxSlots = new ArrayList<>();
        goodSlots = new ArrayList<>();

        this.tradeboxInventory = new SimpleInventory(4) {
            public void markDirty() {
                super.markDirty();
                TradeBoxScreenHandler.this.onContentChanged(this);
                TradeBoxScreenHandler.this.inventoryChangeListener.run();
            }
        };
        checkSize(goodInventory, 8);

        AtomicInteger num = new AtomicInteger();
        //Init tradeboxSlots
        IntStream.range(0, 2).forEach(i ->
                IntStream.range(0, 2).forEach(j -> {
                    this.tradeboxSlots.add(this.addSlot(new Slot(tradeboxInventory, num.get(), 25 + i * 18, 26 + j * 18) {
                        public boolean canInsert(ItemStack stack) {
                            return true;
                        }
                    }));
                    num.getAndIncrement();
                }));
        //Init goodSlots
        num.set(0);
        IntStream.range(0, 2).forEach(j ->
                IntStream.range(0, 4).forEach(i -> {
                    this.goodSlots.add(this.addSlot(new Slot(goodInventory, num.get(), 85 + i * 18, 26 + j * 18) {
                        public boolean canInsert(ItemStack stack) {
                            return false;
                        }

                        public boolean canTakeItems(PlayerEntity playerEntity) {
                            return false;
                        }

                        public boolean isEnabled() {
                            return !isCooling();
                        }
                    }));
                    num.getAndIncrement();
                }));
        //Init player inventory
        IntStream.range(0, 3).forEach(m ->
                IntStream.range(0, 9).forEach(l -> this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18))));
        //Init player hot bar
        IntStream.range(0, 9).forEach(m -> this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 142)));

        //Generate trade mission if tradebox is in trading process but has illegal trade mission
        if (isTrading() && !hasLegalTradeMission()) {
            setTradeboxTrading();
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    public int getCoolingTime() {
        return propertyDelegate.get(0);
    }

    public int getProcess() {
        return propertyDelegate.get(3);
    }

    @Environment(EnvType.CLIENT)
    public void setInventoryChangeListener(Runnable inventoryChangeListener) {
        this.inventoryChangeListener = inventoryChangeListener;
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        checkTradeMission();
        this.sendContentUpdates();
    }

    public void checkTradeMission() {
        if (!isTrading())
            return;

        //Test whether the inputted goods meet required goods
        Map<Item, Integer> inputGoodMap = goodSlotListToGoodMap(tradeboxSlots);
        Map<Item, Integer> neededGoodMap = goodSlotListToGoodMap(getToLocationGoodSlots());
        if (!TradeboxManager.test(inputGoodMap, neededGoodMap)) {
            return;
        }

        //Set process cooling first so that does't trigger this method by itself
        setProcess(TradeboxEntity.PROCESS_COOLING);
        //Consume input goods
        consumeInputGood(neededGoodMap);
        //Drop extra input goods to player
        this.context.run((world, blockPos) -> {
            this.dropInventory(player, this.tradeboxInventory);
        });
        //Set goods from location into tradeboxSlots
        int goodNum = 0;
        for (Slot slot : getFromLocationGoodSlots()) {
            if (slot.hasStack()) {
                this.tradeboxSlots.get(goodNum).setStack(slot.getStack());
                goodNum++;
            }
        }

        setTradeboxCooling();
    }

    public void consumeInputGood(Map<Item, Integer> targetGoodMap) {
        for (Map.Entry<Item, Integer> targetGood : targetGoodMap.entrySet()) {
            int currentNum = 0;
            int requiredNum = targetGood.getValue();
            for (Slot slot : tradeboxSlots) {
                if (targetGood.getKey().equals(slot.getStack().getItem())) {
                    ItemStack itemStack = slot.getStack();
                    if (itemStack.getCount() >= targetGood.getValue()) {
                        currentNum = requiredNum;
                        slot.takeStack(requiredNum);
                    } else {
                        currentNum += itemStack.getCount();
                        slot.takeStack(itemStack.getCount());
                    }
                }
                if (currentNum == requiredNum) {
                    break;
                }
            }
        }
    }

    public boolean isCooling() {
        return getProcess() == TradeboxEntity.PROCESS_COOLING;
    }

    public boolean isTrading() {
        return getProcess() == TradeboxEntity.PROCESS_TRADING;
    }

    public void setProcess(int process) {
        this.propertyDelegate.set(3, process);
    }

    public void setCoolingTime(int coolingTime) {
        this.propertyDelegate.set(0, coolingTime);
    }

    public void setLocationId(int locationId) {
        this.propertyDelegate.set(1, locationId);
    }

    public void setResidentId(int residentId) {
        this.propertyDelegate.set(2, residentId);
    }

    public int getLocationId() {
        return propertyDelegate.get(1);
    }

    public int getResidentId() {
        return propertyDelegate.get(2);
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        if (!player.world.isClient) {
            player.world.playSound(null, new BlockPos(player.getPos()), DrinkBeer.TRADEBOX_CLOSE_EVENT, SoundCategory.BLOCKS, 0.6f, 1f);
        }
        this.context.run((world, blockPos) -> {
            this.dropInventory(player, this.tradeboxInventory);
        });
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.tradeboxSlots.size()) {
                if (!this.insertItem(originalStack, this.tradeboxSlots.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.tradeboxSlots.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }

    public void setTradeMission(TradeMission tradeMission) {
        setLocationId(tradeMission.getLocationId());
        setResidentId(tradeMission.getResidentId());
        if (!tradeMission.getGoodToLocationList().isEmpty()) {
            IntStream.range(0, tradeMission.getGoodToLocationList().size()).forEach(i -> {
                Good good = tradeMission.getGoodToLocationList().get(i);
                ItemStack goodItemStack = new ItemStack(good.getGoodItem(), good.getCount());
                goodSlots.get(i).setStack(goodItemStack);
            });
        }
        if (!tradeMission.getGoodFromLocationList().isEmpty()) {
            IntStream.range(0, tradeMission.getGoodFromLocationList().size()).forEach(i -> {
                Good good = tradeMission.getGoodFromLocationList().get(i);
                ItemStack goodItemStack = new ItemStack(good.getGoodItem(), good.getCount());
                goodSlots.get(4 + i).setStack(goodItemStack);
            });
        }
    }

    public void setTradeboxCooling() {
        setProcess(TradeboxEntity.PROCESS_COOLING);
        setCoolingTime(TradeboxManager.COOLING_TIME_ON_REFRESH);
        setLocationId(Locations.EMPTY_LOCATION.getId());
        setResidentId(Residents.EMPTY_RESIDENT.getId());
        clearGoodInventory();
    }

    public void setTradeboxTrading() {
        TradeMission tradeMission = new TradeMission();
        try {
            Block block = player.world.getBlockState(pos).getBlock();
            if (block.asItem().equals(DrinkBeer.TRADE_BOX_NORMAL.asItem())) {
                tradeMission = TradeMission.genRandomTradeMission();
            }
            /*else if(block.asItem().equals(DrinkBeer.TRADE_BOX_NORTHON.asItem()){
                tradeMission = TradeMission.genSpecificTradeMission(Locations.NORTHON.getId());
            }*/
        } catch (Exception e) {
            tradeMission = TradeMission.genRandomTradeMission();
        }
        setTradeMission(tradeMission);
        setProcess(TradeboxEntity.PROCESS_TRADING);
    }

    public List<Slot> getToLocationGoodSlots() {
        return goodSlots.subList(0, 4);
    }

    public List<Slot> getFromLocationGoodSlots() {
        return goodSlots.subList(4, 8);
    }

    public boolean hasLegalTradeMission() {
        if (!(Math.max(getLocationId(), Locations.EMPTY_LOCATION.getId()) == Math.min(getLocationId(), Locations.size())))
            return false;
        if (!(Math.max(getResidentId(), Residents.EMPTY_RESIDENT.getId()) == Math.min(getResidentId(), Residents.size())))
            return false;
        if (getToLocationGoodSlots().stream().noneMatch(Slot::hasStack))
            return false;
        return getFromLocationGoodSlots().stream().anyMatch(Slot::hasStack);
    }

    private void clearGoodInventory() {
        goodSlots.forEach(slot -> slot.setStack(ItemStack.EMPTY));
    }

    private Map<Item, Integer> goodSlotListToGoodMap(List<Slot> goodSlots) {
        Map<Item, Integer> goodMap = new HashMap<>();
        IntStream.range(0, 4).forEach(i -> {
            if (!goodSlots.get(i).hasStack())
                return;

            ItemStack inputTradeboxSlotsItemStack = goodSlots.get(i).getStack();
            Item inputTradeboxSlotsItem = inputTradeboxSlotsItemStack.getItem();
            if (goodMap.containsKey(inputTradeboxSlotsItem)) {
                goodMap.replace(inputTradeboxSlotsItem, goodMap.get(inputTradeboxSlotsItem) + inputTradeboxSlotsItemStack.getCount());
            } else {
                goodMap.put(inputTradeboxSlotsItem, inputTradeboxSlotsItemStack.getCount());
            }
        });
        return goodMap;
    }
}
