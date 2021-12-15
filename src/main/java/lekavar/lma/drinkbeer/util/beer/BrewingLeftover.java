package lekavar.lma.drinkbeer.util.beer;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum BrewingLeftover {
    WATER_BUCKET(Items.WATER_BUCKET, Items.BUCKET),
    MILK_BUCKET(Items.MILK_BUCKET,Items.BUCKET);

    private final Item oriItem;
    private final Item brewingLeftOverItem;

    BrewingLeftover(Item oriItem, Item brewingLeftoverItem) {
        this.oriItem = oriItem;
        this.brewingLeftOverItem = brewingLeftoverItem;
    }

    public Item getOriItem() {
        return oriItem;
    }

    public Item getBrewingLeftOverItem() {
        return brewingLeftOverItem;
    }

    public static List<BrewingLeftover> getBrewingLeftoverList() {
        List<BrewingLeftover> brewingLeftoverList = new ArrayList();
        Collections.addAll(brewingLeftoverList, values());
        return brewingLeftoverList;
    }
}
