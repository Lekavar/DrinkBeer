package lekavar.lma.drinkbeer.util.tradebox;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.jetbrains.annotations.NotNull;

public class Good {
    private Item goodItem;
    private Locations location;
    private Residents resident;
    private int fromOrToLocation;
    private int minCount;
    private int maxCount;
    private int rarity;

    public final static int FROM_LOCATION = 1;
    public final static int TO_LOCATION = 2;
    public final static int FROM_AND_TO_LOCATION = 3;
    private final static int MIN_COUNT = 1;

    public Good(){
        this.goodItem = Items.AIR;
        this.location = Locations.EMPTY_LOCATION;
        this.resident = Residents.EMPTY_RESIDENT;
        this.fromOrToLocation = FROM_LOCATION;
        this.minCount = MIN_COUNT;
        this.maxCount = MIN_COUNT;
        this.rarity = GoodsRarity.DEFAULT_RARITY;
    }

    public Good setGoodItem(Item goodItem) {
        this.goodItem = goodItem;
        return this;
    }

    public Good setTradeLocation(@NotNull Locations location,@NotNull int fromOrToLocation) {
        this.location = location;
        this.fromOrToLocation = fromOrToLocation;
        return this;
    }

    public Good setResident(@NotNull Residents resident) {
        this.resident = resident;
        return this;
    }

    public Good setCountRange(@NotNull int minCount,@NotNull int maxCount) {
        this.maxCount = maxCount;
        return this;
    }

    public Good setRarity(int rarity) {
        this.rarity = rarity;
        return this;
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
}
