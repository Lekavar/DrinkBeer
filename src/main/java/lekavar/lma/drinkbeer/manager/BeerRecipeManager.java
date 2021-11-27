package lekavar.lma.drinkbeer.manager;

import lekavar.lma.drinkbeer.util.beer.BeerRecipe;
import lekavar.lma.drinkbeer.util.beer.Beers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;

import java.util.List;
import java.util.Map;

public class BeerRecipeManager {
    public static BeerRecipe matchBeerRecipe(Map<Item, Integer> inputMaterialMap, PlayerEntity player) {
        BeerRecipe beerRecipeResult;

        //Match basic recipe
        BeerRecipe tBeerRecipe = match(inputMaterialMap);
        //Check bonus(from armors,status effects...)
        if (tBeerRecipe != null) {
            //Do something
        }

        beerRecipeResult = tBeerRecipe;
        return beerRecipeResult;
    }

    private static BeerRecipe match(Map<Item, Integer> inputMaterialMap) {
        BeerRecipe beerRecipeResult = null;
        for (Beers beer : Beers.values()) {
            Map<List<Item>, Integer> materialMap = beer.getBeerRecipe().getMaterialMap();
            if (test(materialMap, inputMaterialMap)) {
                beerRecipeResult = beer.getBeerRecipe().copy();
                break;
            }
        }
        return beerRecipeResult;
    }

    private static boolean test(Map<List<Item>, Integer> materialMap, Map<Item, Integer> inputMaterialMap) {
        for (Map.Entry<List<Item>, Integer> materials : materialMap.entrySet()) {
            int requiredNum = materials.getValue();
            int inputNum = 0;
            for (Item material : materials.getKey()) {
                if (inputMaterialMap.containsKey(material)) {
                    inputNum = inputNum + inputMaterialMap.get(material);
                }
            }
            if (requiredNum != inputNum) {
                return false;
            }
        }
        return true;
    }
}
