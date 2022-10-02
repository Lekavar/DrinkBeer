package lekavar.lma.drinkbeer.util.tradebox;

import java.util.Random;

public class GoodsRarity {
    public static final int NORMAL = 1;
    public static final int RARE = 2;
    public static final int SUPER_RARE = 3;
    public static final int UNIQUE = 4;

    public static final int DEFAULT_RARITY = NORMAL;
    public static final int EMPTY_RARITY = 0;
    public static final int GOOD_FROM_LOCATION_RARITY_WEIGHT_SUM = getRarityWeight(NORMAL) + getRarityWeight(RARE) + getRarityWeight(SUPER_RARE) + getRarityWeight(UNIQUE);
    public static final int GOOD_TO_LOCATION_RARITY_WEIGHT_SUM = getRarityWeight(NORMAL) + getRarityWeight(RARE);

    public static int getRarityWeight(int rarity) {
        switch (rarity) {
            case NORMAL:
                return 49;
            case RARE:
                return 30;
            case SUPER_RARE:
                return 15;
            case UNIQUE:
                return 6;
            default:
                return 49;
        }
    }

    public static int genRandomRarity(int fromOrToLocation) {
        int sumWeight = fromOrToLocation == Good.FROM ? GOOD_FROM_LOCATION_RARITY_WEIGHT_SUM : GOOD_TO_LOCATION_RARITY_WEIGHT_SUM;
        int point = new Random().nextInt(sumWeight) + 1;
        if (point <= getRarityWeight(NORMAL)) {
            return NORMAL;
        } else if (point > getRarityWeight(NORMAL) && point <= (getRarityWeight(NORMAL) + getRarityWeight(RARE))) {
            return RARE;
        } else if (point > (getRarityWeight(NORMAL) + getRarityWeight(RARE)) && point <= (getRarityWeight(NORMAL) + getRarityWeight(RARE) + getRarityWeight(SUPER_RARE))) {
            return SUPER_RARE;
        } else if (point > (getRarityWeight(NORMAL) + getRarityWeight(RARE) + getRarityWeight(SUPER_RARE))) {
            return UNIQUE;
        } else {
            return NORMAL;
        }
    }
}
