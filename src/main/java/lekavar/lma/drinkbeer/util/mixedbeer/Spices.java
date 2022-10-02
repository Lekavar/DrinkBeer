package lekavar.lma.drinkbeer.util.mixedbeer;

import lekavar.lma.drinkbeer.DrinkBeer;
import net.minecraft.item.Item;

public enum Spices {
    BLAZE_PAPRIKA(1, DrinkBeer.SPICE_BLAZE_PAPRIKA.asItem(), Flavors.FIERY),
    DRIED_EGLIA_BUD(2, DrinkBeer.SPICE_DRIED_EGLIA_BUD.asItem(), Flavors.SPICY),
    SMOKED_EGLIA_BUD(3, DrinkBeer.SPICE_SMOKED_EGLIA_BUD.asItem(), Flavors.FIERY),
    AMETHYST_NIGELLA_SEEDS(4, DrinkBeer.SPICE_AMETHYST_NIGELLA_SEEDS.asItem(), Flavors.AROMITIC),
    CITRINE_NIGELLA_SEEDS(5, DrinkBeer.SPICE_CITRINE_NIGELLA_SEEDS.asItem(), Flavors.AROMITIC1),
    ICE_MINT(6, DrinkBeer.SPICE_ICE_MINT.asItem(), Flavors.REFRESHING),
    ICE_PATCHOULI(7, DrinkBeer.SPICE_ICE_PATCHOULI.asItem(), Flavors.REFRESHING1),
    STORM_SHARDS(8, DrinkBeer.SPICE_STORM_SHARDS.asItem(), Flavors.STORMY),
    ROASTED_RED_PINE_NUTS(9, DrinkBeer.SPICE_ROASTED_RED_PINE_NUTS.asItem(), Flavors.NUTTY),
    GLACE_GOJI_BERRIES(10, DrinkBeer.SPICE_GLACE_GOJI_BERRIES.asItem(), Flavors.SWEET),
    FROZEN_PERSIMMON(11, DrinkBeer.SPICE_FROZEN_PERSIMMON.asItem(), Flavors.LUSCIOUS),
    ROASTED_PECANS(12, DrinkBeer.SPICE_ROASTED_PECANS.asItem(), Flavors.NUTTY1),
    SILVER_NEEDLE_WHITE_TEA(13, DrinkBeer.SPICE_SILVER_NEEDLE_WHITE_TEA.asItem(), Flavors.MELLOW),
    GOLDEN_CINNAMON_POWDER(14, DrinkBeer.SPICE_GOLDEN_CINNAMON_POWDER.asItem(), Flavors.SWEET),
    DRIED_SELAGINELLA(15, DrinkBeer.SPICE_DRIED_SELAGINELLA.asItem(), Flavors.DRYING);

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
