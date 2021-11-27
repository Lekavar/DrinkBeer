package lekavar.lma.drinkbeer.util.tradebox;

import lekavar.lma.drinkbeer.DrinkBeer;

public enum Goods {
    TWIGVALLEY_BLAZE_PAPRIKA(1, new Good().setGoodItem(DrinkBeer.SPICE_BLAZE_PAPRIKA.asItem())
            .setTradeLocation(Locations.TWIGVALLEY,Good.FROM_LOCATION)
            .setCountRange(2,4).setRarity(GoodsRarity.RARE));

    private final int id;
    private final Good good;

    Goods(int id, Good good) {
        this.id = id;
        this.good = good;
    }

    public int getId() {
        return id;
    }

    public Good getGood() {
        return good;
    }
}
