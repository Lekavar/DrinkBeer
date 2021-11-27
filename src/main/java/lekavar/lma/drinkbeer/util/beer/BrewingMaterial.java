package lekavar.lma.drinkbeer.util.beer;

import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BrewingMaterial {
    public static final List<Item> MATERIAL_LIST = initMaterialList();

    private static List<Item> initMaterialList() {
        Beers[] beers = Beers.values();
        List<Item> materialList = new ArrayList<>();

        for (Beers beer : beers) {
            Map<List<Item>, Integer> materialMap = beer.getBeerRecipe().getMaterialMap();
            for (List<Item> materials : materialMap.keySet()) {
                insertMaterialList(materials, materialList);
            }
        }

        return materialList;
    }

    private static void insertMaterialList(List<Item> materials, List<Item> materialList) {
        if (!materialList.isEmpty()) {
            for (Item material : materials) {
                if (!materialList.contains(material))
                    materialList.add(material);
            }
        } else {
            materialList.addAll(materials);
        }
    }
}
