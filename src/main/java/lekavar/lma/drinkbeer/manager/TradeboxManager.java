package lekavar.lma.drinkbeer.manager;

import lekavar.lma.drinkbeer.util.tradebox.Good;
import lekavar.lma.drinkbeer.util.tradebox.Goods;
import lekavar.lma.drinkbeer.util.tradebox.Locations;
import lekavar.lma.drinkbeer.util.tradebox.Residents;
import net.minecraft.item.Item;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TradeboxManager {
    public static final int COOLING_TIME_ON_PLACE = 6000;
    public static final int COOLING_TIME_ON_REFRESH = 4800;

    private static List<Good> getGoodListByCondition(@Nullable Integer locationId, @Nullable Integer residentId, @Nullable Integer rarity, @Nullable Integer fromOrToLocation) {
        List<Good> goodList = Goods.getList();
        return filterGoodList(goodList, locationId, residentId, rarity, fromOrToLocation);
    }

    public static List<Good> getGoodListByResident(@NotNull Integer residentId, @NotNull Integer fromOrToLocation) {
        return getGoodListByCondition(Residents.byId(residentId).getLocation().getId(), residentId, null, fromOrToLocation);
    }

    private static List<Good> filterGoodList(@NotNull List<Good> goodList, @Nullable Integer locationId, @Nullable Integer residentId, @Nullable Integer rarity, @Nullable Integer fromOrToLocation) {
        List<Good> resultGoodList = new ArrayList<>(goodList);

        if (locationId != null) {
            int finalLocationId = locationId;
            resultGoodList = resultGoodList.stream().filter(good -> good.getLocation().getId() == finalLocationId).collect(Collectors.toList());
        }
        if (residentId != null) {
            int finalResidentId = residentId;
            resultGoodList = resultGoodList.stream().filter(good -> good.getResident() == null || good.getResident().getId() == finalResidentId).collect(Collectors.toList());
        }
        if (rarity != null) {
            int finalRarity = rarity;
            resultGoodList = resultGoodList.stream().filter(good -> good.getRarity() == finalRarity).collect(Collectors.toList());
        }
        if (fromOrToLocation != null) {
            int finalFromOrToLocation = fromOrToLocation;
            resultGoodList = resultGoodList.stream().filter(good -> good.getFromOrToLocation() == finalFromOrToLocation).collect(Collectors.toList());
        }

        return resultGoodList;
    }

    public static List<Good> filterGoodListByRarity(@NotNull List<Good> goodList, @NotNull Integer rarity) {
        return filterGoodList(goodList, null, null, rarity, null);
    }

    public static String getLocationTranslationKey(int locationId) {
        return getLocationTranslationKey(Locations.byId(locationId));
    }

    public static String getLocationTranslationKey(Locations location) {
        return "drinkbeer.location." + location.getName();
    }

    public static String getResidentTranslationKey(int residentId) {
        return getResidentTranslationKey(Residents.byId(residentId));
    }

    public static String getResidentTranslationKey(Residents resident) {
        return "drinkbeer.resident." + resident.getName();
    }

    public static boolean test(Map<Item, Integer> inputGoodMap, Map<Item, Integer> neededGoodMap) {
        for (Map.Entry<Item, Integer> neededGood : neededGoodMap.entrySet()) {
            if (!inputGoodMap.containsKey(neededGood.getKey())) {
                return false;
            } else if (!(inputGoodMap.get(neededGood.getKey()) >= neededGood.getValue())) {
                return false;
            }
        }
        return true;
    }
}
