package lekavar.lma.drinkbeer.util.tradebox;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.jetbrains.annotations.NotNull;

public class Good {
    private Item goodItem;
    private Locations location;
    private Residents resident;
    private int fromOrToLocation;
    private int count;
    private int minCount;
    private int maxCount;
    private int rarity;

    public final static int EMPTY_FROM_OR_TO = 0;
    public final static int FROM = 1;
    public final static int TO = 2;
    private final static int MIN_COUNT = 1;

    public Good() {
        this.goodItem = Items.AIR;
        this.location = Locations.EMPTY_LOCATION;
        this.resident = Residents.EMPTY_RESIDENT;
        this.fromOrToLocation = FROM;
        this.count = MIN_COUNT;
        this.minCount = MIN_COUNT;
        this.maxCount = MIN_COUNT;
        this.rarity = GoodsRarity.DEFAULT_RARITY;
    }

    public Good set(Block goodBlock, @NotNull Locations location, @NotNull int fromOrToLocation, Residents resident, @NotNull int minCount, @NotNull int maxCount, int rarity) {
        return this.set(goodBlock.asItem(), location, fromOrToLocation, resident, minCount, maxCount, rarity);
    }

    public Good set(Item goodItem, @NotNull Locations location, @NotNull int fromOrToLocation, Residents resident, @NotNull int minCount, @NotNull int maxCount, int rarity) {
        return this.setGoodItem(goodItem).setTradeLocation(location, fromOrToLocation).setResident(resident).setCountRange(minCount, maxCount).setRarity(rarity);
    }

    public Good setGoodItem(Item goodItem) {
        this.goodItem = goodItem;
        return this;
    }

    public Good setGoodItem(Block goodBlock) {
        return setGoodItem(goodBlock.asItem());
    }

    public Good setTradeLocation(@NotNull Locations location, @NotNull int fromOrToLocation) {
        this.location = location;
        this.fromOrToLocation = fromOrToLocation;
        return this;
    }

    public Good setResident(@NotNull Residents resident) {
        this.resident = resident;
        return this;
    }

    public Good setCountRange(@NotNull int minCount, @NotNull int maxCount) {
        this.minCount = Math.max(minCount, MIN_COUNT);
        this.maxCount = maxCount;
        return this;
    }

    public Good setRarity(int rarity) {
        this.rarity = Math.min(Math.max(rarity, GoodsRarity.NORMAL), GoodsRarity.UNIQUE);
        return this;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public Item getGoodItem() {
        return goodItem;
    }

    public Locations getLocation() {
        return location;
    }

    public Residents getResident() {
        return resident;
    }

    public int getFromOrToLocation() {
        return fromOrToLocation;
    }

    public int getMinCount() {
        return minCount;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public int getRarity() {
        return rarity;
    }

    public Good copy(Good good) {
        this.set(good.getGoodItem(), good.getLocation(), good.getFromOrToLocation(), good.getResident(), good.getMinCount(), good.getMaxCount(), good.getRarity());
        this.setCount(good.getCount());
        return this;
    }
}
