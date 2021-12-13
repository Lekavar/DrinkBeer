package lekavar.lma.drinkbeer.util.tradebox;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public enum Residents {
    EMPTY(0, "empty", Locations.EMPTY),
    LEKA(1, "leka", Locations.NORTHON),
    HAAR(2, "haar", Locations.NORTHON),
    ANU(3, "anu", Locations.TWIGVALLEY),
    WILL(4, "will", Locations.TWIGVALLEY);

    private final int id;
    private final String name;
    private final Locations location;

    public final static Residents EMPTY_RESIDENT = EMPTY;

    Residents(int id, String name, Locations location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Locations getLocation() {
        return location;
    }

    public static Residents byId(int id) {
        Residents[] residents = values();
        for (Residents resident : residents) {
            if (resident.id == id) {
                return resident;
            }
        }
        return EMPTY_RESIDENT;
    }

    public static List<Residents> getList() {
        return Arrays.stream(values()).collect(Collectors.toList());
    }

    public static List<Residents> getList(Locations location) {
        return Arrays.stream(values()).filter(resident -> resident.getLocation().equals(location)).collect(Collectors.toList());
    }

    public static List<Residents> getList(int locationId) {
        return Arrays.stream(values()).filter(resident -> resident.getLocation().getId() == (locationId)).collect(Collectors.toList());
    }

    public static int size() {
        return values().length;
    }

    public static int genRandomResidentId(int locationId) {
        int residentId = Residents.EMPTY_RESIDENT.getId();
        List<Residents> residentsList = Residents.getList(locationId);
        if (residentsList != null) {
            if (!residentsList.isEmpty()) {
                residentId = residentsList.get(new Random().nextInt(residentsList.size())).getId();
            }
        }
        return residentId;
    }
}
