package lekavar.lma.drinkbeer.util.mixedbeer;

import lekavar.lma.drinkbeer.DrinkBeer;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;

public enum Flavors {
    SPICY(1, "spicy", null, ParticleTypes.LAVA),
    FIERY(2, "fiery", SPICY, ParticleTypes.LAVA),
    SOOOOO_SPICY(3, "soooo_spicy", null, null),
    AROMITIC(4, "aromatic", null, DrinkBeer.MIXED_BEER_DEFAULT),
    AROMITIC1(5, "aromatic1", AROMITIC, DrinkBeer.MIXED_BEER_DEFAULT),
    REFRESHING(6, "refreshing", null, DrinkBeer.MIXED_BEER_DEFAULT),
    REFRESHING1(7, "refreshing1", REFRESHING, DrinkBeer.MIXED_BEER_DEFAULT),
    STORMY(8, "stormy", null, DrinkBeer.MIXED_BEER_DEFAULT),
    THE_FALL_OF_THE_GIANT(9, "the_fall_of_the_giant", null, null),
    NUTTY(10, "nutty", null, DrinkBeer.MIXED_BEER_DEFAULT),
    SWEET(11, "sweet", null, DrinkBeer.MIXED_BEER_DEFAULT),
    LUSCIOUS(12, "luscious", SWEET, DrinkBeer.MIXED_BEER_DEFAULT),
    CLOYING(13,"cloying",null,null),
    NUTTY1(14, "nutty1", NUTTY, DrinkBeer.MIXED_BEER_DEFAULT),
    MELLOW(15,"mellow",null,DrinkBeer.MIXED_BEER_DEFAULT),
    DRYING(16,"drying",null,DrinkBeer.MIXED_BEER_DEFAULT);

    private final int id;
    private final String name;
    private final Flavors fatherFlavor;
    private final DefaultParticleType particle;

    public static final int EMPTY_FLAVOR_ID = 0;
    public static final Flavors DEFAULT_FLAVOR = Flavors.SPICY;
    public static final DefaultParticleType DEFAULT_PARTICLE = DrinkBeer.MIXED_BEER_DEFAULT;

    Flavors(int id, String name, Flavors fatherFlavor, DefaultParticleType particle) {
        this.id = id;
        this.name = name;
        this.fatherFlavor = fatherFlavor;
        this.particle = particle;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Flavors getFatherFlavor() {
        return fatherFlavor;
    }

    public DefaultParticleType getParticle() {
        return particle;
    }

    public static Flavors byId(int id) {
        Flavors[] flavors = values();
        for (Flavors flavor : flavors) {
            if (flavor.id == id) {
                return flavor;
            }
        }
        return DEFAULT_FLAVOR;
    }
}
