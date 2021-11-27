package lekavar.lma.drinkbeer.util.tradebox;

public enum Residents {
    LEKA(1, "leka", Locations.NORTHON),
    HAAR(2, "haar", Locations.NORTHON);

    private final int id;
    private final String name;
    private final Locations location;

    public final static Residents EMPTY_RESIDENT = null;

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
}
