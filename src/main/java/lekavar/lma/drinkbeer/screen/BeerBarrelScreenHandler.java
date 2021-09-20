package lekavar.lma.drinkbeer.screen;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import lekavar.lma.drinkbeer.DrinkBeer;
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

import java.util.*;

public class BeerBarrelScreenHandler extends ScreenHandler {
    private final ScreenHandlerContext context;
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
    private Runnable inventoryChangeListener;
    private PlayerEntity player;

    public BeerBarrelScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public BeerBarrelScreenHandler(int syncId, PlayerInventory playerInventory, final ScreenHandlerContext context) {
        this(syncId, playerInventory, context, new ArrayPropertyDelegate(4));
    }

    public BeerBarrelScreenHandler(int syncId, PlayerInventory playerInventory, final ScreenHandlerContext context, PropertyDelegate propertyDelegate) {
        super(DrinkBeer.BEER_BARREL_SCREEN_HANDLER, syncId);
        this.inventoryChangeListener = () -> {
        };
        this.propertyDelegate = propertyDelegate;
        this.addProperties(propertyDelegate);
        this.player = playerInventory.player;
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
        this.context = context;
        //Our inventory
        this.materialSlot1 = this.addSlot(new Slot(input, 0, 28, 26) {
            public boolean canInsert(ItemStack stack) {
                if (!isBrewing()) {
                    return createMaterialMap().containsKey(stack.getItem());
                } else
                    return false;
            }
        });
        this.materialSlot2 = this.addSlot(new Slot(input, 1, 46, 26) {
            public boolean canInsert(ItemStack stack) {
                if (!isBrewing()) {
                    return createMaterialMap().containsKey(stack.getItem());
                } else
                    return false;
            }
        });
        this.materialSlot3 = this.addSlot(new Slot(input, 2, 28, 44) {
            public boolean canInsert(ItemStack stack) {
                if (!isBrewing()) {
                    return createMaterialMap().containsKey(stack.getItem());
                } else
                    return false;
            }
        });
        this.materialSlot4 = this.addSlot(new Slot(input, 3, 46, 44) {
            public boolean canInsert(ItemStack stack) {
                if (!isBrewing()) {
                    return createMaterialMap().containsKey(stack.getItem());
                } else
                    return false;
            }
        });
        this.emptyMugSlot = this.addSlot(new Slot(input, 4, 73, 50) {
            public boolean canInsert(ItemStack stack) {
                if (stack.getItem().asItem() == DrinkBeer.EMPTY_BEER_MUG.asItem() && isMaterialCompleted() && !isBrewing())
                    return true;
                else
                    return false;
            }
        });
        this.resultSlot = this.addSlot(new Slot(output, 0, 128, 34) {
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            public boolean canTakeItems(PlayerEntity playerEntity) {
                if (isBrewing() && !isBrewingTimeRemain()) {
                    return true;
                } else
                    return false;
            }

            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                resetBeerBarrel();
            }

        });
        if (this.propertyDelegate.get(2) != 0) {
            BiMap<Integer, Item> beerTypeMap = createBeerTypeMap();
            ItemStack resultItemStack = new ItemStack(beerTypeMap.get(this.propertyDelegate.get(2)), 4);
            this.resultSlot.setStack(resultItemStack);
        }
        //The player inventory
        int m;
        int l;
        for (m = 0; m < 3; ++m) {
            for (l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
            }
        }
        //The player Hotbar
        for (m = 0; m < 9; ++m) {
            this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 142));
        }
    }

    public void resetBeerBarrel() {
        this.propertyDelegate.set(1, 0);
        this.propertyDelegate.set(2, 0);
        this.propertyDelegate.set(3, 0);
    }

    public boolean isBrewing() {
        return this.propertyDelegate.get(3) == 1 ? true : false;
    }

    public boolean isBrewingTimeRemain() {
        return this.propertyDelegate.get(0) > 0 ? true : false;
    }

    public boolean isMaterialCompleted() {
        if (this.propertyDelegate.get(1) == 0)
            return false;
        else
            return true;
    }

    public int getSyncedNumber() {
        return propertyDelegate.get(0);
    }

    public List<ItemStack> getBackResult() {
        Map<Item, Integer> map = new HashMap<>();
        ItemStack materialItemStack1 = this.materialSlot1.getStack();
        ItemStack materialItemStack2 = this.materialSlot2.getStack();
        ItemStack materialItemStack3 = this.materialSlot3.getStack();
        ItemStack materialItemStack4 = this.materialSlot4.getStack();
        Item materialItem1 = materialItemStack1.getItem();
        Item materialItem2 = materialItemStack2.getItem();
        Item materialItem3 = materialItemStack3.getItem();
        Item materialItem4 = materialItemStack4.getItem();
        map.put(materialItem1, 1);
        if (map.containsKey(materialItem2)) {
            map.put(materialItem2, map.get(materialItem2) + 1);
        } else {
            map.put(materialItem2, 1);
        }
        if (map.containsKey(materialItem3)) {
            map.put(materialItem3, map.get(materialItem3) + 1);
        } else {
            map.put(materialItem3, 1);
        }
        if (map.containsKey(materialItem4)) {
            map.put(materialItem4, map.get(materialItem4) + 1);
        } else {
            map.put(materialItem4, 1);
        }

        List<ItemStack> getBackResultList = new ArrayList<>();
        if (map.containsKey(Items.WATER_BUCKET)) {
            ItemStack itemStackWaterBucket = new ItemStack(Items.BUCKET, map.get(Items.WATER_BUCKET));
            getBackResultList.add(itemStackWaterBucket);
        }
        if (map.containsKey(Items.MILK_BUCKET)) {
            ItemStack itemStackMilkBucket = new ItemStack(Items.BUCKET, map.get(Items.MILK_BUCKET));
            getBackResultList.add(itemStackMilkBucket);
        }

        return getBackResultList;
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

    @Environment(EnvType.CLIENT)
    public void setInventoryChangeListener(Runnable inventoryChangeListener) {
        this.inventoryChangeListener = inventoryChangeListener;
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        updateResultSlot();
        //this.sendContentUpdates();
    }

    public void updateResultSlot() {
        if (isBrewing())
            return;

        this.propertyDelegate.set(1, 0);
        this.resultSlot.setStack(ItemStack.EMPTY);

        ItemStack materialItemStack1 = this.materialSlot1.getStack();
        ItemStack materialItemStack2 = this.materialSlot2.getStack();
        ItemStack materialItemStack3 = this.materialSlot3.getStack();
        ItemStack materialItemStack4 = this.materialSlot4.getStack();
        Map<Item, Integer> map = new HashMap<>();
        Item materialItem1 = materialItemStack1.getItem();
        Item materialItem2 = materialItemStack2.getItem();
        Item materialItem3 = materialItemStack3.getItem();
        Item materialItem4 = materialItemStack4.getItem();
        map.put(materialItem1, 1);
        if (map.containsKey(materialItem2)) {
            map.put(materialItem2, map.get(materialItem2) + 1);
        } else {
            map.put(materialItem2, 1);
        }
        if (map.containsKey(materialItem3)) {
            map.put(materialItem3, map.get(materialItem3) + 1);
        } else {
            map.put(materialItem3, 1);
        }
        if (map.containsKey(materialItem4)) {
            map.put(materialItem4, map.get(materialItem4) + 1);
        } else {
            map.put(materialItem4, 1);
        }

        ItemStack resultItemStack = getResult(map);
        if (!resultItemStack.isEmpty()) {
            this.resultSlot.setStack(resultItemStack);
            this.propertyDelegate.set(1, 1);
        } else {
            //doNothing
        }

        if (isMaterialCompleted()) {
            ItemStack emptyMugItemStack = this.emptyMugSlot.getStack();
            if (emptyMugItemStack.getCount() >= 4) {
                Item beerItem = this.resultSlot.getStack().getItem();
                BiMap<Item, Integer> beerTypeMap = createBeerTypeMap().inverse();
                this.propertyDelegate.set(0, getBrewingTimeInResultSlot());
                this.propertyDelegate.set(2, beerTypeMap.get(beerItem));
                this.propertyDelegate.set(3, 1);


                this.getBackResultList = getBackResult();

                BeerBarrelScreenHandler.this.materialSlot1.takeStack(1);
                BeerBarrelScreenHandler.this.materialSlot2.takeStack(1);
                BeerBarrelScreenHandler.this.materialSlot3.takeStack(1);
                BeerBarrelScreenHandler.this.materialSlot4.takeStack(1);
                BeerBarrelScreenHandler.this.emptyMugSlot.takeStack(4);

                this.resultSlot.markDirty();
            }
        }
    }

    public int getBrewingTimeInResultSlot(){
        Item beerItem = this.resultSlot.getStack().getItem();
        if(beerItem!=Items.AIR) {
            Map<Item, Integer> brewingTimeMap = createBrewingTimeMap();
            return brewingTimeMap.get(beerItem);
        }
        return 0;
    }

    public static Map<Item, String> createMaterialMap() {
        Map<Item, String> map = Maps.newLinkedHashMap();
        map.put(Items.WATER_BUCKET, "water_bucket");
        map.put(Items.MILK_BUCKET, "milk_bucket");
        map.put(Items.BLAZE_POWDER, "blaze_powder");
        map.put(Items.WHEAT, "wheat");
        map.put(Items.SUGAR, "sugar");
        map.put(Items.APPLE, "apple");
        map.put(Items.SWEET_BERRIES, "sweet_berrires");
        return map;
    }

    public static Map<Item, Integer> createBrewingTimeMap() {
        Map<Item, Integer> map = Maps.newLinkedHashMap();
        map.put(DrinkBeer.BEER_MUG.asItem(), 24000);
        map.put(DrinkBeer.BEER_MUG_BLAZE_STOUT.asItem(), 12000);
        map.put(DrinkBeer.BEER_MUG_BLAZE_MILK_STOUT.asItem(), 18000);
        map.put(DrinkBeer.BEER_MUG_APPLE_LAMBIC.asItem(), 24000);
        map.put(DrinkBeer.BEER_MUG_SWEET_BERRY_KRIEK.asItem(), 24000);
        return map;
    }

    public static BiMap<Integer, Item> createBeerTypeMap() {
        BiMap<Integer, Item> map = HashBiMap.create();
        map.put(1, DrinkBeer.BEER_MUG.asItem());
        map.put(2, DrinkBeer.BEER_MUG_BLAZE_STOUT.asItem());
        map.put(3, DrinkBeer.BEER_MUG_BLAZE_MILK_STOUT.asItem());
        map.put(4, DrinkBeer.BEER_MUG_APPLE_LAMBIC.asItem());
        map.put(5, DrinkBeer.BEER_MUG_SWEET_BERRY_KRIEK.asItem());
        return map;
    }

    private ItemStack getResult(Map<Item, Integer> map) {
        //adding new recipes starts here
        if (map.containsKey(Items.WATER_BUCKET)) {
            if (map.get(Items.WATER_BUCKET) == 1) {
                if (map.containsKey(Items.WHEAT)) {
                    if (map.get(Items.WHEAT) == 3) {
                        return new ItemStack(DrinkBeer.BEER_MUG.asItem(), 4);
                    } else if (map.get(Items.WHEAT) == 2) {
                        if (map.containsKey(Items.BLAZE_POWDER))
                            return new ItemStack(DrinkBeer.BEER_MUG_BLAZE_STOUT.asItem(), 4);
                        if (map.containsKey(Items.APPLE))
                            return new ItemStack(DrinkBeer.BEER_MUG_APPLE_LAMBIC.asItem(), 4);
                        if (map.containsKey(Items.SWEET_BERRIES))
                            return new ItemStack(DrinkBeer.BEER_MUG_SWEET_BERRY_KRIEK.asItem(), 4);
                    } else if (map.get(Items.WHEAT) == 1) {
                        if (map.containsKey(Items.BLAZE_POWDER) && map.containsKey(Items.SUGAR)) {
                            return new ItemStack(DrinkBeer.BEER_MUG_BLAZE_MILK_STOUT.asItem(), 4);
                        }
                    }
                }

            }
        }
        //adding new recipes ends here
        return ItemStack.EMPTY;
    }

    public void close(PlayerEntity player) {
        super.close(player);
        if (this.isMaterialCompleted() && !isBrewing()) {
            this.propertyDelegate.set(1, 0);
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
}
