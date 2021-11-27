package lekavar.lma.drinkbeer.util.beer;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class BeerRecipe {
    private Item beerResult;
    private Map<List<Item>, Integer> materialMap;
    private int beerResultNum;
    private int brewingTime;

    private final static int MIN_BEER_RESULT_NUM = 1;
    private final static int MIN_BREWING_TIME = 0;
    public final static int DEFAULT_BREWING_TIME = 24000;

    public BeerRecipe() {
        this.beerResult = Items.AIR;
        this.materialMap = new HashMap<>();
        this.beerResultNum = MIN_BEER_RESULT_NUM;
        this.brewingTime = DEFAULT_BREWING_TIME;
    }

    public Item getBeerResult() {
        return beerResult;
    }

    public int getBeerResultNum() {
        return beerResultNum;
    }

    public int getBrewingTime() {
        return brewingTime;
    }

    public Map<List<Item>, Integer> getMaterialMap() {
        return materialMap;
    }

    public void setBeerResult(Item beerResult) {
        this.beerResult = beerResult;
    }

    public BeerRecipe setBeerResultNum(int beerResultNum) {
        this.beerResultNum = Math.max(beerResultNum, MIN_BEER_RESULT_NUM);
        return this;
    }

    public BeerRecipe setBrewingTime(int brewingTime) {
        this.brewingTime = Math.max(brewingTime, MIN_BREWING_TIME);
        return this;
    }

    public BeerRecipe addMaterial(@NotNull Item... materials) {
        return addMaterial(1, materials);
    }

    public BeerRecipe addMaterial(int num, @NotNull Item... materials) {
        List<Item> materialList = new ArrayList<>();
        Collections.addAll(materialList, materials);
        return addMaterial(num, materialList);
    }

    private BeerRecipe addMaterial(int num, List<Item> materialList) {
        this.materialMap.put(materialList, num);
        return this;
    }

    public BeerRecipe copy(){
        BeerRecipe newBeerRecipe = new BeerRecipe();
        newBeerRecipe.beerResult = this.beerResult;
        newBeerRecipe.brewingTime = this.brewingTime;
        newBeerRecipe.materialMap = this.materialMap;
        newBeerRecipe.beerResultNum = this.beerResultNum;
        return newBeerRecipe;
    }
}
