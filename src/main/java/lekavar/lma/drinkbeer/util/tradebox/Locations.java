package lekavar.lma.drinkbeer.util.tradebox;

public enum Locations {
    NORTHON(1, "northon", LocationDistance.FAR),
    TWIGVALLEY(2, "twigvalley", LocationDistance.CLOSE);

    private final int id;
    private final String name;
    private final int distance;

    public final static Locations EMPTY_LOCATION = null;

    Locations(int id, String name, int distance) {
        this.id = id;
        this.name = name;
        this.distance = distance;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getDistance() {
        return distance;
    }
}
