package lekavar.lma.drinkbeer.manager;

import lekavar.lma.drinkbeer.util.tradebox.Good;
import lekavar.lma.drinkbeer.util.tradebox.Locations;
import lekavar.lma.drinkbeer.util.tradebox.Residents;
import net.minecraft.text.Text;

import java.util.List;

public class TradeBoxTestManager {
    public static void PrintGoodList() {
        System.out.println("|  ■■■ Drinkbeer: Start to print good list ■■■");
        System.out.println("|  -------------------------------------------------------------------------------");
        List<Locations> locationList = Locations.getList();
        if (locationList != null) {
            if (!locationList.isEmpty()) {
                for (Locations location : locationList) {
                    if (location.equals(Locations.EMPTY_LOCATION))
                        continue;
                    System.out.println("|");
                    System.out.println("|  ■■ Location:" + Text.translatable(TradeboxManager.getLocationTranslationKey(location)).getString() + " ■■");
                    List<Residents> residentList = Residents.getList(location);
                    if (residentList != null) {
                        if (!residentList.isEmpty()) {
                            for (Residents resident : residentList) {
                                System.out.println("|");
                                System.out.println("|  ■ Resident:" + Text.translatable(TradeboxManager.getResidentTranslationKey(resident)).getString() + " ■");
                                List<Good> GoodFromLocationList = TradeboxManager.getGoodListByResident(resident.getId(), Good.TO);
                                System.out.println("|  ❤ " + Text.translatable(TradeboxManager.getResidentTranslationKey(resident)).getString() + " wants ❤");
                                for (Good good : GoodFromLocationList) {
                                    System.out.println("|  |  " + good.getGoodItem().getName().getString()
                                            + ", " + (good.getMinCount() == good.getMaxCount() ? good.getMinCount() : (good.getMinCount() + "-" + good.getMaxCount()))
                                            + ", " + getRarityName(good.getRarity())
                                    );
                                }
                                System.out.println("|  😀 " + Text.translatable(TradeboxManager.getResidentTranslationKey(resident)).getString() + " will give 😀");
                                List<Good> GoodToLocationList = TradeboxManager.getGoodListByResident(resident.getId(), Good.FROM);
                                for (Good good : GoodToLocationList) {
                                    System.out.println("|  |  " + good.getGoodItem().getName().getString()
                                            + ", " + (good.getMinCount() == good.getMaxCount() ? good.getMinCount() : (good.getMinCount() + "-" + good.getMaxCount()))
                                            + ", " + getRarityName(good.getRarity())
                                    );
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println("|");
        System.out.println("|  -------------------------------------------------------------------------------");
        System.out.println("|  ■■■ Drinkbeer: Finish printing good list ■■■");
    }

    private static String getRarityName(int rarity) {
        switch (rarity) {
            case 1:
                return "normal";
            case 2:
                return "rare";
            case 3:
                return "super rare";
            case 4:
                return "unique";
            default:
                return "undefined";
        }
    }
}
