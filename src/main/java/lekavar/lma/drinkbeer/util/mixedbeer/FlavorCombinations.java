package lekavar.lma.drinkbeer.util.mixedbeer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum FlavorCombinations {
    SOOOOO_SPICY(Flavors.SOOOOO_SPICY, new FlavorCombination()
            .addFlavorCombination(true, Flavors.SPICY, Flavors.SPICY, Flavors.SPICY)),
    FURY_OF_THE_STORM(Flavors.THE_FALL_OF_THE_GIANT, new FlavorCombination()
            .addFlavorCombination(true, Flavors.STORMY, Flavors.STORMY, Flavors.STORMY)),
    CLOYING(Flavors.CLOYING, new FlavorCombination()
            .addFlavorCombination(true, Flavors.SWEET, Flavors.SWEET, Flavors.SWEET));

    private final Flavors combinedFlavor;
    private final FlavorCombination flavorCombination;

    FlavorCombinations(Flavors combinedFlavor, FlavorCombination flavorCombination) {
        this.combinedFlavor = combinedFlavor;
        this.flavorCombination = flavorCombination;
    }

    public Flavors getCombinedFlavor() {
        return combinedFlavor;
    }

    public FlavorCombination getFlavorCombination() {
        return flavorCombination;
    }

    public static List<FlavorCombinations> getFlavorCombinationList() {
        List<FlavorCombinations> flavorCombinationList = new ArrayList();
        Collections.addAll(flavorCombinationList, values());
        return flavorCombinationList;
    }
}
