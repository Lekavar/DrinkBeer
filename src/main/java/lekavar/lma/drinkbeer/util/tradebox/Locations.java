package lekavar.lma.drinkbeer.util.tradebox;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public enum Locations {
    EMPTY(0,"empty"),
    NORTHON(1, "northon"),
    TWIGVALLEY(2, "twigvalley");

    private final int id;
    private final String name;

    public final static Locations EMPTY_LOCATION = EMPTY;

    Locations(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public static Locations byId(int id) {
        Locations[] locations = values();
        for (Locations location : locations) {
            if (location.id == id) {
                return location;
            }
        }
        return EMPTY_LOCATION;
    }

    public static List<Locations> getList(){
        return Arrays.stream(values()).collect(Collectors.toList());
    }

    public static int size(){
        return values().length;
    }

    public static int genRandomLocationId(){
        return new Random().nextInt(Locations.size() - 1) + 1;
    }
}
