package lekavar.lma.drinkbeer.util.tradebox;

import net.minecraft.item.Items;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static lekavar.lma.drinkbeer.DrinkBeer.*;
import static lekavar.lma.drinkbeer.util.tradebox.Good.FROM;
import static lekavar.lma.drinkbeer.util.tradebox.Good.TO;
import static lekavar.lma.drinkbeer.util.tradebox.GoodsRarity.*;
import static lekavar.lma.drinkbeer.util.tradebox.Locations.NORTHON;
import static lekavar.lma.drinkbeer.util.tradebox.Locations.TWIGVALLEY;
import static lekavar.lma.drinkbeer.util.tradebox.Residents.HAAR;
import static lekavar.lma.drinkbeer.util.tradebox.Residents.LEKA;

public enum Goods {
    /*Northon*/
    /*-------------------------------------------------------------------------------------------------------------------*/
    //From
    /*----------------------------------------------*/
    NORTHON_AMETHYST_NIGELLA_SEEDS(new Good().set(SPICE_AMETHYST_NIGELLA_SEEDS, NORTHON, FROM, null, 1, 3, NORMAL)),
    NORTHON_ICE_MINT(new Good().set(SPICE_ICE_MINT, NORTHON, FROM, null, 1, 3, NORMAL)),

    NORTHON_ICE_PATCHOULI(new Good().set(SPICE_ICE_PATCHOULI, NORTHON, FROM, null, 1, 2, RARE)),
    NORTHON_CITRINE_NIGELLA_SEEDS(new Good().set(SPICE_CITRINE_NIGELLA_SEEDS, NORTHON, FROM, null, 1, 2, RARE)),

    NORTHON_STORM_SHARDS(new Good().set(SPICE_STORM_SHARDS, NORTHON, FROM, null, 1, 2, SUPER_RARE)),
    NORTHON_BLUE_ICE(new Good().set(Items.BLUE_ICE,NORTHON,FROM,null,1,2,SUPER_RARE)),

    NORTHON_LAKAS_CALL_BELL(new Good().set(LEKAS_CALL_BELL, NORTHON, FROM, LEKA, 1, 1, UNIQUE)),
    //To
    /*----------------------------------------------*/
    NORTHON_COAL(new Good().set(Items.COAL, NORTHON, TO, null, 2, 5, NORMAL)),
    NORTHON_CHARCOAL(new Good().set(Items.CHARCOAL, NORTHON, TO, null, 1, 4, NORMAL)),
    NORTHON_WHITE_WOOL(new Good().set(Items.WHITE_WOOL,NORTHON,TO,LEKA,1,2,NORMAL)),
    NORTHON_BONE(new Good().set(Items.BONE,NORTHON,TO,LEKA,2,4,NORMAL)),
    NORTHON_GUNPOWDER(new Good().set(Items.GUNPOWDER,NORTHON,TO,HAAR,1,3,NORMAL)),
    NORTHON_GOLD_NUGGET(new Good().set(Items.GOLD_NUGGET,NORTHON,TO,HAAR,4,9,NORMAL)),

    NORTHON_NIGHT_HOWL_KVASS(new Good().set(BEER_MUG_NIGHT_HOWL_KVASS, NORTHON, TO, LEKA, 1, 1, RARE)),
    NORTHON_HAARS_ICEY_PALE_LAGER(new Good().set(BEER_MUG_HAARS_ICEY_PALE_LAGER, NORTHON, TO, HAAR, 1, 1, RARE)),
    NORTHON_GOLD_INGOT(new Good().set(Items.GOLD_INGOT,NORTHON,TO,HAAR,1,1,RARE)),
    /*Twigvalley*/
    /*-------------------------------------------------------------------------------------------------------------------*/
    //From
    /*----------------------------------------------*/
    TWIGVALLEY_DRIED_EGLIA_BUD(new Good().set(SPICE_DRIED_EGLIA_BUD, TWIGVALLEY, FROM, null, 2, 3, NORMAL)),
    TWIGVALLEY_SMOKED_EGLIA_BUD(new Good().set(SPICE_SMOKED_EGLIA_BUD, TWIGVALLEY, FROM, null, 1, 3, NORMAL)),

    TWIGVALLEY_BLAZE_PAPRIKA(new Good().set(SPICE_BLAZE_PAPRIKA, TWIGVALLEY, FROM, null, 1, 2, RARE)),
    //To
    /*----------------------------------------------*/
    TWIGVALLEY_APPLE(new Good().set(Items.APPLE, TWIGVALLEY, TO, null, 1, 2, NORMAL));

    private final Good good;

    Goods(Good good) {
        this.good = good;
    }

    public Good getGood() {
        return good;
    }

    public static List<Good> getList(){
        return Arrays.stream(values()).map(good -> good.getGood()).collect(Collectors.toList());
    }
}
