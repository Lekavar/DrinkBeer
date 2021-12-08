package lekavar.lma.drinkbeer.util.tradebox;

import lekavar.lma.drinkbeer.manager.TradeboxManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class TradeMission {
    private int locationId;
    private int residentId;
    private List<Good> goodToLocationList;
    private List<Good> goodFromLocationList;

    public TradeMission() {
        this.locationId = Locations.EMPTY_LOCATION.getId();
        this.residentId = Residents.EMPTY_RESIDENT.getId();
        this.goodToLocationList = new ArrayList<>();
        this.goodFromLocationList = new ArrayList<>();
    }

    private void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    private void setResidentId(int residentId) {
        this.residentId = residentId;
    }

    private void setGoodToLocationList(List<Good> goodToLocationList) {
        this.goodToLocationList = goodToLocationList;
    }

    private void setGoodFromLocationList(List<Good> goodFromLocationList) {
        this.goodFromLocationList = goodFromLocationList;
    }

    public int getLocationId() {
        return locationId;
    }

    public int getResidentId() {
        return residentId;
    }

    public List<Good> getGoodFromLocationList() {
        return goodFromLocationList;
    }

    public List<Good> getGoodToLocationList() {
        return goodToLocationList;
    }

    public static TradeMission genRandomTradeMission() {
        return genTradeMission(Locations.EMPTY_LOCATION.getId());
    }

    public static TradeMission genSpecificTradeMission(int locationId) {
        return genTradeMission(locationId);
    }

    private static TradeMission genTradeMission(int locationId) {
        TradeMission tradeMission = new TradeMission();
        Random random = new Random();

        //Generate locationId
        locationId = locationId == Locations.EMPTY_LOCATION.getId() ? Locations.genRandomLocationId() : locationId;
        int finalLocationId = locationId;
        //Generate residentId
        int residentId = Residents.genRandomResidentId(locationId);

        //The num of goods to or from location bases on basicGoodNum
        //GoodFromLocationNum should be always greater than or equals to goodToLocationNum
        int basicGoodNum = random.nextInt(3) + 1;
        //Generate num of goods to location
        //1:1/3 2:1/3 3:1/6 4:1/6
        int goodToLocationNum = basicGoodNum + random.nextInt(2);
        //Generate basic goodToLocationList
        List<Good> basicGoodToLocationList = TradeboxManager.getGoodListByResident(residentId, Good.TO);
        //Generate goodTolocationList
        List<Good> goodToLocationList = new ArrayList<>();
        IntStream.range(0, goodToLocationNum).forEach(i -> {
            Good selectedGood = selectRandomGood(basicGoodToLocationList, Good.TO);
            if (selectedGood == null) {
                System.out.println("Has no good to " + Locations.byId(finalLocationId).getName() + "-" + Residents.byId(residentId).getName() + "?");
            } else {
                goodToLocationList.add(selectedGood);
            }
        });

        //Generate num of goods from location
        int goodFromLocationNum = goodToLocationNum == 4 ? 4 : (goodToLocationNum + random.nextInt(2));
        //Generate basic goodFromLocationList
        List<Good> basicGoodFromLocationList = TradeboxManager.getGoodListByResident(residentId, Good.FROM);
        //Generate goodFromlocationList
        List<Good> goodFromLocationList = new ArrayList<>();
        IntStream.range(0, goodFromLocationNum).forEach(i -> {
            Good selectedGood = selectRandomGood(basicGoodFromLocationList, Good.FROM);
            if (selectedGood == null) {
                System.out.println("Has no good from " + Locations.byId(finalLocationId).getName() + "-" + Residents.byId(residentId).getName() + "?");
            } else {
                goodFromLocationList.add(selectedGood);
            }
        });

        //Set proprties
        tradeMission.setLocationId(locationId);
        tradeMission.setResidentId(residentId);
        tradeMission.setGoodToLocationList(goodToLocationList);
        tradeMission.setGoodFromLocationList(goodFromLocationList);

        return tradeMission;
    }

    private static Good selectRandomGood(List<Good> goodList, int fromOrToLocation) {
        Good good = new Good();

        //Generate rarity
        int rarity = GoodsRarity.genRandomRarity(fromOrToLocation);
        //Select good
        List<Good> goods = TradeboxManager.filterGoodListByRarity(goodList, rarity);
        while (goods.isEmpty() && rarity > GoodsRarity.EMPTY_RARITY) {
            rarity--;
            goods = TradeboxManager.filterGoodListByRarity(goodList, rarity);
        }
        if (goods.isEmpty()) {
            return null;
        }
        good.copy(goods.get(new Random().nextInt(goods.size())));
        //Generate good count
        good.setCount(good.getMinCount() + new Random().nextInt(good.getMaxCount() - good.getMinCount() + 1));

        return good;
    }
}
