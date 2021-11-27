package lekavar.lma.drinkbeer.util.mixedbeer;

import lekavar.lma.drinkbeer.DrinkBeer;
import lekavar.lma.drinkbeer.util.beer.BrewingLeftover;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Spices {
    BLAZE_PAPRIKA(1, DrinkBeer.SPICE_BLAZE_PAPRIKA.asItem(), Flavors.FIERY),
    DRIED_EGLIA_BUD(2, DrinkBeer.SPICE_DRIED_EGLIA_BUD.asItem(), Flavors.SPICY),
    SMOKED_EGLIA_BUD(3, DrinkBeer.SPICE_SMOKED_EGLIA_BUD.asItem(), Flavors.FIERY),
    AMETHYST_NIGELLA_SEEDS(4, DrinkBeer.SPICE_AMETHYST_NIGELLA_SEEDS.asItem(), Flavors.AROMITIC),
    CITRINE_NIGELLA_SEEDS(5, DrinkBeer.SPICE_CITRINE_NIGELLA_SEEDS.asItem(), Flavors.AROMITIC1),
    ICE_MINT(6, DrinkBeer.SPICE_ICE_MINT.asItem(), Flavors.REFRESHING),
    ICE_PATCHOULI(7, DrinkBeer.SPICE_ICE_PATCHOULI.asItem(), Flavors.REFRESHING1),
    STORM_SHARDS(8, DrinkBeer.SPICE_STORM_SHARDS.asItem(), Flavors.STORMY);

    private final int id;
    private final Item spiceItem;
    private final Flavors flavor;

    public static final int EMPTY_SPICE_ID = 0;
    public static final Spices DEFAULT_SPICE = Spices.BLAZE_PAPRIKA;

    Spices(int id, Item spiceItem, Flavors flavor) {
        this.id = id;
        this.spiceItem = spiceItem;
        this.flavor = flavor;
    }

    public int getId() {
        return this.id;
    }

    public Item getSpiceItem() {
        return spiceItem;
    }

    public Flavors getFlavor() {
        return flavor;
    }

    public static Spices byId(int id) {
        Spices[] spices = values();
        for (Spices spice : spices) {
            if (spice.id == id) {
                return spice;
            }
        }
        return DEFAULT_SPICE;
    }

    public static Spices byItem(Item spiceItem) {
        Spices[] spices = values();
        for (Spices spice : spices) {
            if (spice.spiceItem.equals(spiceItem)) {
                return spice;
            }
        }
        return DEFAULT_SPICE;
    }

    public static int size() {
        return values().length;
    }
}
