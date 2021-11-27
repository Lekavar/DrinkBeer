package lekavar.lma.drinkbeer.screen;

import lekavar.lma.drinkbeer.DrinkBeer;
import lekavar.lma.drinkbeer.item.MixedBeerBlockItem;
import lekavar.lma.drinkbeer.item.SpiceBlockItem;
import lekavar.lma.drinkbeer.manager.MixedBeerManager;
import lekavar.lma.drinkbeer.util.beer.Beers;
import lekavar.lma.drinkbeer.util.mixedbeer.Spices;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;

import java.util.ArrayList;
import java.util.List;

public class BartendingTableScreenHandler extends ScreenHandler {
    private final Inventory input;
    private final Inventory output;
    private final Slot beerInputSlot;
    private final Slot spiceInputSlot1;
    private final Slot spiceInputSlot2;
    private final Slot spiceInputSlot3;
    private final Slot resultSlot;
    private int[] isUsed;

    private final ScreenHandlerContext context;
    private Runnable inventoryChangeListener;

    public BartendingTableScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public BartendingTableScreenHandler(int syncId, PlayerInventory playerInventory, final ScreenHandlerContext context) {
        this(syncId, playerInventory, Beers.EMPTY_BEER_ID, 0, null, context);
    }

    public BartendingTableScreenHandler(int syncId, PlayerInventory playerInventory, int beerId, int isMixedBeer, List<Integer> spiceList, final ScreenHandlerContext context) {
        super(DrinkBeer.BARTENDING_TABLE_SCREEN_HANDLER, syncId);

        this.inventoryChangeListener = () -> {
        };
        this.context = context;
        ItemStack oriBeerItemStack = isMixedBeer == 1 ? MixedBeerManager.genMixedBeerItemStack(beerId, spiceList) : new ItemStack(Beers.byId(beerId).getBeerItem(), 1);
        this.isUsed = new int[3];
        resetIsUsed();

        this.input = new SimpleInventory(4) {
            public void markDirty() {
                super.markDirty();
                BartendingTableScreenHandler.this.onContentChanged(this);
                BartendingTableScreenHandler.this.inventoryChangeListener.run();
            }
        };
        this.output = new SimpleInventory(1) {
            public void markDirty() {
                super.markDirty();
                BartendingTableScreenHandler.this.inventoryChangeListener.run();
            }
        };

        //Init beerInput slot
        this.beerInputSlot = this.addSlot(new Slot(input, 0, 27, 34) {
            public boolean canInsert(ItemStack stack) {
                return canInsertBeerInputSlot(stack);
            }
        });
        this.beerInputSlot.setStack(oriBeerItemStack);
        //Init spiceInput slots
        this.spiceInputSlot1 = this.addSlot(new Slot(input, 1, 67, 16) {
            public boolean canInsert(ItemStack stack) {
                return canInsertSpiceInputSlots(stack);
            }
        });
        this.spiceInputSlot2 = this.addSlot(new Slot(input, 2, 67, 34) {
            public boolean canInsert(ItemStack stack) {
                return canInsertSpiceInputSlots(stack);
            }
        });
        this.spiceInputSlot3 = this.addSlot(new Slot(input, 3, 67, 52) {
            public boolean canInsert(ItemStack stack) {
                return canInsertSpiceInputSlots(stack);
            }
        });
        //Init result slot
        this.resultSlot = this.addSlot(new Slot(output, 0, 128, 34) {
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                takeInputStack();
            }
        });
        this.resultSlot.setStack(oriBeerItemStack);

        //Init player inventory
        int m;
        int l;
        for (m = 0; m < 3; ++m) {
            for (l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
            }
        }
        //Init player hot bar
        for (m = 0; m < 9; ++m) {
            this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 142));
        }
    }

    private boolean canInsertBeerInputSlot(ItemStack stack) {
        try {
            assert stack.getItem().getGroup() != null;
            if (stack.getItem().getGroup().equals(DrinkBeer.DRINK_BEER)) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    private boolean canInsertSpiceInputSlots(ItemStack stack) {
        //Check if there's vacancy of spice
        if (getBeerInputSpiceList().size() + getInputSpicesNum() >= MixedBeerManager.MAX_SPICES_COUNT) {
            return false;
        }
        //Check if is spice
        return stack.getItem() instanceof SpiceBlockItem;
    }

    private int getInputSpicesNum() {
        int num = 0;
        for (int i = 0; i < MixedBeerManager.MAX_SPICES_COUNT; i++) {
            if (!getSpiceItemStackInSpiceSlot(i).equals(ItemStack.EMPTY)) {
                num++;
            }
        }
        return num;
    }

    private ItemStack getSpiceItemStackInSpiceSlot(int index) {
        return switch (index) {
            case 0 -> spiceInputSlot1 == null ? spiceInputSlot1.getStack().getItem().equals(Items.AIR) || spiceInputSlot1.getStack().equals(ItemStack.EMPTY) ? ItemStack.EMPTY : spiceInputSlot1.getStack() : spiceInputSlot1.getStack();
            case 1 -> spiceInputSlot2 == null ? spiceInputSlot2.getStack().getItem().equals(Items.AIR) || spiceInputSlot2.getStack().equals(ItemStack.EMPTY) ? ItemStack.EMPTY : spiceInputSlot2.getStack() : spiceInputSlot2.getStack();
            case 2 -> spiceInputSlot3 == null ? spiceInputSlot3.getStack().getItem().equals(Items.AIR) || spiceInputSlot3.getStack().equals(ItemStack.EMPTY) ? ItemStack.EMPTY : spiceInputSlot3.getStack() : spiceInputSlot3.getStack();
            default -> ItemStack.EMPTY;
        };
    }

    /**
     * Check if input beer is mixed beer
     *
     * @return true: Is mixed beer false: Is not mixed beer
     */
    private boolean isMixedBeer() {
        return this.beerInputSlot.getStack().getItem() instanceof MixedBeerBlockItem;
    }

    private List<Integer> getBeerInputSpiceList() {
        List<Integer> inputSpiceList = new ArrayList<>();
        if (isMixedBeer()) {
            return MixedBeerManager.getSpiceList(this.beerInputSlot.getStack());
        } else {
            return inputSpiceList;
        }
    }

    private int getBeerInputBeerId() {
        if (isMixedBeer()) {
            return MixedBeerManager.getBeerId(this.beerInputSlot.getStack());
        } else
            return Beers.byItem(this.beerInputSlot.getStack().getItem()).getId();
    }

    public void takeInputStack() {
        beerInputSlot.takeStack(1);
        if (beerInputSlot.getStack().getItem().equals(Items.AIR)) {
            beerInputSlot.setStack(ItemStack.EMPTY);
        }
        if (isUsed[0] == 1) {
            spiceInputSlot1.takeStack(1);
            if (spiceInputSlot1.getStack().getItem().equals(Items.AIR)) {
                spiceInputSlot1.setStack(ItemStack.EMPTY);
            }
        }
        if (isUsed[1] == 1) {
            spiceInputSlot2.takeStack(1);
            if (spiceInputSlot2.getStack().getItem().equals(Items.AIR)) {
                spiceInputSlot2.setStack(ItemStack.EMPTY);
            }
        }
        if (isUsed[2] == 1) {
            spiceInputSlot3.takeStack(1);
            if (spiceInputSlot3.getStack().getItem().equals(Items.AIR)) {
                spiceInputSlot3.setStack(ItemStack.EMPTY);
            }
        }
    }

    public void updateResultSlot() {
        //Result slot hasn't initialized yet, return
        if (this.resultSlot == null) {
            return;
        }
        //Input beer is empty, return
        if (this.beerInputSlot.getStack() == ItemStack.EMPTY || this.beerInputSlot.getStack().getItem().equals(Items.AIR)) {
            this.resultSlot.setStack(ItemStack.EMPTY);
            return;
        }

        //Mark all spices in spiceSlot are not used, so they are currently not marked to be consumed
        resetIsUsed();
        int beerId = getBeerInputBeerId();
        ItemStack resultStack;
        //If is basic liquor and there's no spices, the result is basic liquor itself
        if (!isMixedBeer() && getInputSpicesNum() == 0) {
            resultStack = new ItemStack(Beers.byId(beerId).getBeerItem(), 1);
        }
        //Otherwise the result must be mixed beer
        else {
            List<Integer> oriSpiceList = new ArrayList<>();
            oriSpiceList = isMixedBeer() ? getBeerInputSpiceList() : oriSpiceList;
            for (int i = 0; i < MixedBeerManager.MAX_SPICES_COUNT; i++) {
                ItemStack stack = getSpiceItemStackInSpiceSlot(i);
                //If maximum amount of spice is already in beer, there's no need to check spiceSlot
                if (oriSpiceList.size() < MixedBeerManager.MAX_SPICES_COUNT) {
                    //Check if spiceSlot is not empty
                    if (stack != ItemStack.EMPTY) {
                        //If there are enough spices, insert new spice in the middle or at the start
                        if (i < oriSpiceList.size()) {
                            oriSpiceList.add(i, Spices.byItem(stack.getItem()).getId());
                        }
                        //Otherwise insert new spice at the end
                        else {
                            oriSpiceList.add(Spices.byItem(stack.getItem()).getId());
                        }
                        //Mark spice in spiceSlot#i will be consumed to mix beer
                        isUsed[i] = 1;
                    }
                }
            }
            //Generate result item stack by beerId and spiceList
            resultStack = MixedBeerManager.genMixedBeerItemStack(beerId, oriSpiceList);
        }
        this.resultSlot.setStack(resultStack);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.input.size()) {
                if (!this.insertItem(originalStack, this.input.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.input.size(), false)) {
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

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        updateResultSlot();
        this.sendContentUpdates();
    }

    @Environment(EnvType.CLIENT)
    public void setInventoryChangeListener(Runnable inventoryChangeListener) {
        this.inventoryChangeListener = inventoryChangeListener;
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        this.context.run((world, blockPos) -> {
            this.dropInventory(player, this.input);
        });
    }

    public void resetIsUsed() {
        if (this.isUsed != null) {
            this.isUsed[0] = 0;
            this.isUsed[1] = 0;
            this.isUsed[2] = 0;
        }
    }
}
