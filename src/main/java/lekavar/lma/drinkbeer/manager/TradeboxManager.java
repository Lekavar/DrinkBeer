package lekavar.lma.drinkbeer.manager;

import lekavar.lma.drinkbeer.util.tradebox.Locations;
import lekavar.lma.drinkbeer.util.tradebox.Residents;

public class TradeboxManager {
    private static final int DISTANCE_TO_TIME_RATE = 1200;

    public static int getTravelTime(Residents residents) {
        Locations location = residents.getLocation();
        return getTravelTime(location);
    }

    public static int getTravelTime(Locations location) {
        int distance = location.getDistance();
        //TODO 随机增减时间
        return distance * DISTANCE_TO_TIME_RATE;
    }
}
