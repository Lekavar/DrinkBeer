package lekavar.lma.drinkbeer.screen;

import lekavar.lma.drinkbeer.DrinkBeer;
import lekavar.lma.drinkbeer.manager.BeerRecipeManager;
import lekavar.lma.drinkbeer.util.beer.BeerRecipe;
import lekavar.lma.drinkbeer.util.beer.Beers;
import lekavar.lma.drinkbeer.util.beer.BrewingLeftover;
import lekavar.lma.drinkbeer.util.beer.BrewingMaterial;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.*;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;

import java.util.*;

public class BeerBarrelScreenHandler extends ScreenHandler {
    private final Inventory input;
    private final Inventory output;
    private List<ItemStack> getBackResultList;
    private final Slot materialSlot1;
    private final Slot materialSlot2;
    private final Slot materialSlot3;
    private final Slot materialSlot4;
    private final Slot emptyMugSlot;
    private final Slot resultSlot;
    PropertyDelegate propertyDelegate;
    BeerRecipe beerRecipe;

    private final ScreenHandlerContext context;
    private Runnable inventoryChangeListener;
    private PlayerEntity player;

    public BeerBarrelScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public BeerBarrelScreenHandler(int syncId, PlayerInventory playerInventory, final ScreenHandlerContext context) {
        this(syncId, playerInventory, context, new ArrayPropertyDelegate(5));
    }

    public BeerBarrelScreenHandler(int syncId, PlayerInventory playerInventory, final ScreenHandlerContext context, PropertyDelegate propertyDelegate) {
        super(DrinkBeer.BEER_BARREL_SCREEN_HANDLER, syncId);

        this.beerRecipe = null;
        this.propertyDelegate = propertyDelegate;
        this.addProperties(propertyDelegate);
        this.input = new SimpleInventory(5) {
            public void markDirty() {
                super.markDirty();
                BeerBarrelScreenHandler.this.onContentChanged(this);
                BeerBarrelScreenHandler.this.inventoryChangeListener.run();
            }
        };
        this.output = new SimpleInventory(1) {
            public void markDirty() {
                super.markDirty();
                BeerBarrelScreenHandler.this.inventoryChangeListener.run();
            }
        };

        this.inventoryChangeListener = () -> {
        };
        this.context = context;
        this.player = playerInventory.player;

        //Init input slots
        this.materialSlot1 = this.addSlot(new Slot(input, 0, 28, 26) {
            public boolean canInsert(ItemStack stack) {
                return canInsertMaterialSlot(stack);
            }
        });
        this.materialSlot2 = this.addSlot(new Slot(input, 1, 46, 26) {
            public boolean canInsert(ItemStack stack) {
                return canInsertMaterialSlot(stack);
            }
        });
        this.materialSlot3 = this.addSlot(new Slot(input, 2, 28, 44) {
            public boolean canInsert(ItemStack stack) {
                return canInsertMaterialSlot(stack);
            }
        });
        this.materialSlot4 = this.addSlot(new Slot(input, 3, 46, 44) {
            public boolean canInsert(ItemStack stack) {
                return canInsertMaterialSlot(stack);
            }
        });
        this.emptyMugSlot = this.addSlot(new Slot(input, 4, 73, 50) {
            public boolean canInsert(ItemStack stack) {
                return canInsertEmptyMugSlot(stack);
            }
        });
        //Init result slot
        this.resultSlot = this.addSlot(new Slot(output, 0, 128, 34) {
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            public boolean canTakeItems(PlayerEntity playerEntity) {
                return canTakeItemsResultSlot();
            }

            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                playPouringSound(player, stack);
                resetBeerBarrel();
                super.onTakeItem(player, stack);
            }

        });
        if (getBeerId() != 0) {
            Beers beer = Beers.byId(getBeerId());
            ItemStack resultItemStack = new ItemStack(beer.getBeerItem(), this.getBeerResultNum());
            this.resultSlot.setStack(resultItemStack);
        }

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

    public void resetBeerBarrel() {
        setIsMaterialCompleted(false);
        setBeerId(Beers.EMPTY_BEER_ID);
        setIsBrewing(false);
        setBeerResultNum(0);
    }

    public void startBrewing(BeerRecipe beerRecipe) {
        setRemainingBrewingTime(beerRecipe.getBrewingTime());
        setBeerId(Beers.byItem(beerRecipe.getBeerResult()).getId());
        setIsBrewing(true);
        setBeerResultNum(beerRecipe.getBeerResultNum());
    }

    public boolean isBrewing() {
        return getIsBrewing() == 1;
    }

    public boolean isBrewingTimeRemain() {
        return getRemainingBrewingTime() > 0;
    }

    public boolean isMaterialCompleted() {
        return !(getIsMaterialCompleted() == 0);
    }

    public List<ItemStack> getBackResult() {
        Map<Item, Integer> inputMaterialMap = new HashMap<>();
        getInputMaterialMap(inputMaterialMap);

        List<ItemStack> getBackResultList = new ArrayList<>();
        List<BrewingLeftover> brewingLeftoverList = BrewingLeftover.getBrewingLeftoverList();
        for (BrewingLeftover brewingLeftover : brewingLeftoverList) {
            if (inputMaterialMap.containsKey(brewingLeftover.getOriItem())) {
                ItemStack itemStackWaterBucket = new ItemStack(brewingLeftover.getBrewingLeftOverItem(), inputMaterialMap.get(brewingLeftover.getOriItem()));
                getBackResultList.add(itemStackWaterBucket);
            }
        }

        return getBackResultList;
    }

    private void getInputMaterialMap(Map<Item, Integer> inputMaterial) {
        Item materialItem1 = this.materialSlot1.getStack().getItem();
        Item materialItem2 = this.materialSlot2.getStack().getItem();
        Item materialItem3 = this.materialSlot3.getStack().getItem();
        Item materialItem4 = this.materialSlot4.getStack().getItem();
        inputMaterial.put(materialItem1, 1);
        if (inputMaterial.containsKey(materialItem2)) {
            inputMaterial.put(materialItem2, inputMaterial.get(materialItem2) + 1);
        } else {
            inputMaterial.put(materialItem2, 1);
        }
        if (inputMaterial.containsKey(materialItem3)) {
            inputMaterial.put(materialItem3, inputMaterial.get(materialItem3) + 1);
        } else {
            inputMaterial.put(materialItem3, 1);
        }
        if (inputMaterial.containsKey(materialItem4)) {
            inputMaterial.put(materialItem4, inputMaterial.get(materialItem4) + 1);
        } else {
            inputMaterial.put(materialItem4, 1);
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.input.canPlayerUse(player);
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

    public void updateResultSlot() {
        if (isBrewing())
            return;

        setIsMaterialCompleted(false);
        this.resultSlot.setStack(ItemStack.EMPTY);

        Map<Item, Integer> inputMaterialMap = new HashMap<>();
        getInputMaterialMap(inputMaterialMap);

        this.beerRecipe = getBeerRecipeResult(inputMaterialMap);
        if (beerRecipe != null) {
            this.resultSlot.setStack(new ItemStack(beerRecipe.getBeerResult(), beerRecipe.getBeerResultNum()));
            setIsMaterialCompleted(true);

            int requiredEmptyMugNum = beerRecipe.getBeerResultNum();
            ItemStack emptyMugItemStack = this.emptyMugSlot.getStack();
            if (emptyMugItemStack.getCount() >= requiredEmptyMugNum) {
                startBrewing(beerRecipe);

                this.getBackResultList = getBackResult();
                BeerBarrelScreenHandler.this.materialSlot1.takeStack(1);
                BeerBarrelScreenHandler.this.materialSlot2.takeStack(1);
                BeerBarrelScreenHandler.this.materialSlot3.takeStack(1);
                BeerBarrelScreenHandler.this.materialSlot4.takeStack(1);
                BeerBarrelScreenHandler.this.emptyMugSlot.takeStack(requiredEmptyMugNum);

                this.resultSlot.markDirty();
            }
        }
    }

    public int getCurrentBrewingTime() {
        return this.beerRecipe != null ? beerRecipe.getBrewingTime() : 0;
    }

    private BeerRecipe getBeerRecipeResult(Map<Item, Integer> inputMaterialMap) {
        return BeerRecipeManager.matchBeerRecipe(inputMaterialMap, player);
    }

    public void close(PlayerEntity player) {
        super.close(player);
        if (!player.world.isClient) {
            player.world.playSound(null, new BlockPos(player.getPos()), SoundEvents.BLOCK_BARREL_CLOSE, SoundCategory.BLOCKS, 1f, 1f);
        }
        if (this.isMaterialCompleted() && !isBrewing()) {
            setIsMaterialCompleted(false);
            this.resultSlot.markDirty();
        }
        this.context.run((world, blockPos) -> {
            this.dropInventory(player, this.input);
            if (getBackResultList != null) {
                if (getBackResultList.size() > 0) {
                    Inventory getBackInventory = new SimpleInventory(getBackResultList.size());
                    for (int i = 0; i < getBackResultList.size(); i++) {
                        getBackInventory.setStack(i, this.getBackResultList.get(i));
                    }
                    this.dropInventory(player, getBackInventory);
                }
            }
        });
    }

    public int getRemainingBrewingTime() {
        return propertyDelegate.get(0);
    }

    private int getIsMaterialCompleted() {
        return propertyDelegate.get(1);
    }

    private int getBeerId() {
        return propertyDelegate.get(2);
    }

    private int getIsBrewing() {
        return propertyDelegate.get(3);
    }

    private int getBeerResultNum() {
        return propertyDelegate.get(4);
    }

    private void setRemainingBrewingTime(int remainingBrewingTime) {
        this.propertyDelegate.set(0, remainingBrewingTime);
    }

    private void setIsMaterialCompleted(int isMaterialCompleted) {
        this.propertyDelegate.set(1, isMaterialCompleted);
    }

    private void setIsMaterialCompleted(boolean isMaterialCompleted) {
        setIsMaterialCompleted(isMaterialCompleted ? 1 : 0);
    }

    private void setBeerId(int beerId) {
        this.propertyDelegate.set(2, beerId);
    }

    private void setIsBrewing(int isBrewing) {
        this.propertyDelegate.set(3, isBrewing);
    }

    private void setIsBrewing(boolean isBrewing) {
        setIsBrewing(isBrewing ? 1 : 0);
    }

    private void setBeerResultNum(int beerResultNum) {
        this.propertyDelegate.set(4, beerResultNum);
    }

    private boolean canInsertMaterialSlot(ItemStack stack) {
        if (!isBrewing()) {
            return BrewingMaterial.MATERIAL_LIST.contains(stack.getItem());
        } else
            return false;
    }

    private boolean canInsertEmptyMugSlot(ItemStack stack) {
        return stack.getItem().asItem() == DrinkBeer.EMPTY_BEER_MUG.asItem() && isMaterialCompleted() && !isBrewing();
    }

    private boolean canTakeItemsResultSlot() {
        return isBrewing() && !isBrewingTimeRemain();
    }

    private void playPouringSound(PlayerEntity player, ItemStack stack) {
        if (!player.world.isClient) {
            if (stack.getItem().equals(DrinkBeer.BEER_MUG_FROTHY_PINK_EGGNOG.asItem())) {
                player.world.playSound(null, new BlockPos(player.getPos()), DrinkBeer.POURING_CHRISTMAS_EVENT, SoundCategory.BLOCKS, 0.6f, 1f);
            }
            player.world.playSound(null, new BlockPos(player.getPos()), DrinkBeer.POURING_EVENT, SoundCategory.BLOCKS, 1f, 1f);
        }
    }

    @Environment(EnvType.CLIENT)
    public void setInventoryChangeListener(Runnable inventoryChangeListener) {
        this.inventoryChangeListener = inventoryChangeListener;
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        updateResultSlot();
        this.sendContentUpdates();
    }
}
