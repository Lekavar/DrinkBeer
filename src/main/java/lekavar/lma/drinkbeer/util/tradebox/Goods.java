package lekavar.lma.drinkbeer.util.tradebox;

import net.minecraft.item.Items;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static lekavar.lma.drinkbeer.DrinkBeer.*;
import static lekavar.lma.drinkbeer.util.tradebox.Good.*;
import static lekavar.lma.drinkbeer.util.tradebox.GoodsRarity.*;
import static lekavar.lma.drinkbeer.util.tradebox.Locations.*;
import static lekavar.lma.drinkbeer.util.tradebox.Residents.*;

public enum Goods {
    /*Northon*/
    /*-------------------------------------------------------------------------------------------------------------------*/
    //From
    /*----------------------------------------------*/
    NORTHON_AMETHYST_NIGELLA_SEEDS(new Good().set(SPICE_AMETHYST_NIGELLA_SEEDS, NORTHON, FROM, null, 2, 2, NORMAL)),
    NORTHON_ICE_MINT(new Good().set(SPICE_ICE_MINT, NORTHON, FROM, null, 2, 2, NORMAL)),
    NORTHON_ROASTED_RED_PINE_NUTS(new Good().set(SPICE_ROASTED_RED_PINE_NUTS, NORTHON, FROM, null, 2, 2, NORMAL)),
    NORTHON_GLACE_GOJI_BERRIES(new Good().set(SPICE_GLACE_GOJI_BERRIES, NORTHON, FROM, null, 2, 2, NORMAL)),

    NORTHON_ICE_PATCHOULI(new Good().set(SPICE_ICE_PATCHOULI, NORTHON, FROM, null, 1, 2, RARE)),
    NORTHON_CITRINE_NIGELLA_SEEDS(new Good().set(SPICE_CITRINE_NIGELLA_SEEDS, NORTHON, FROM, null, 1, 2, RARE)),
    NORTHON_FROZEN_PERSIMMON(new Good().set(SPICE_FROZEN_PERSIMMON, NORTHON, FROM, null, 1, 2, RARE)),

    NORTHON_BLUE_ICE(new Good().set(Items.BLUE_ICE, NORTHON, FROM, null, 1, 2, SUPER_RARE)),
    NORTHON_SILVER_NEEDLE_WHITE_TEA(new Good().set(SPICE_SILVER_NEEDLE_WHITE_TEA, NORTHON, FROM, null, 1, 2, SUPER_RARE)),
    NORTHON_TNT(new Good().set(Items.TNT, NORTHON, FROM, HAAR, 2, 3, SUPER_RARE)),

    NORTHON_LAKAS_CALL_BELL(new Good().set(LEKAS_CALL_BELL, NORTHON, FROM, LEKA, 1, 1, UNIQUE)),
    NORTHON_STORM_SHARDS(new Good().set(SPICE_STORM_SHARDS, NORTHON, FROM, null, 2, 3, UNIQUE)),
    NORTHON_DIAMOND(new Good().set(Items.DIAMOND, NORTHON, FROM, HAAR, 1, 1, UNIQUE)),
    //To
    /*----------------------------------------------*/
    NORTHON_COAL(new Good().set(Items.COAL, NORTHON, TO, null, 2, 4, NORMAL)),
    NORTHON_CHARCOAL(new Good().set(Items.CHARCOAL, NORTHON, TO, null, 1, 2, NORMAL)),
    NORTHON_WHITE_WOOL(new Good().set(Items.WHITE_WOOL, NORTHON, TO, LEKA, 1, 2, NORMAL)),
    NORTHON_BONE(new Good().set(Items.BONE, NORTHON, TO, LEKA, 2, 3, NORMAL)),
    NORTHON_GUNPOWDER(new Good().set(Items.GUNPOWDER, NORTHON, TO, HAAR, 1, 2, NORMAL)),
    NORTHON_GOLD_NUGGET(new Good().set(Items.GOLD_NUGGET, NORTHON, TO, HAAR, 3, 6, NORMAL)),

    NORTHON_GOLD_INGOT(new Good().set(Items.GOLD_INGOT, NORTHON, TO, HAAR, 1, 1, RARE)),
    /*Twigvalley*/
    /*-------------------------------------------------------------------------------------------------------------------*/
    //From
    /*----------------------------------------------*/
    TWIGVALLEY_DRIED_EGLIA_BUD(new Good().set(SPICE_DRIED_EGLIA_BUD, TWIGVALLEY, FROM, null, 2, 2, NORMAL)),
    TWIGVALLEY_SMOKED_EGLIA_BUD(new Good().set(SPICE_SMOKED_EGLIA_BUD, TWIGVALLEY, FROM, null, 2, 2, NORMAL)),
    TWIGVALLEY_GOLDEN_CINNAMON_POWDER(new Good().set(SPICE_GOLDEN_CINNAMON_POWDER, TWIGVALLEY, FROM, null, 2, 2, NORMAL)),
    TWIGVALLEY_ORANGE_WOOL(new Good().set(Items.ORANGE_WOOL, TWIGVALLEY, FROM, WILL, 2, 3, NORMAL)),
    TWIGVALLEY_ORANGE_TERRACOTTA(new Good().set(Items.ORANGE_TERRACOTTA, TWIGVALLEY, FROM, WILL, 2, 3, NORMAL)),
    TWIGVALLEY_PUMPKIN(new Good().set(Items.PUMPKIN, TWIGVALLEY, FROM, null, 1, 1, NORMAL)),
    TWIGVALLEY_CLAY_BALL(new Good().set(Items.CLAY_BALL, TWIGVALLEY, FROM, null, 5, 8, NORMAL)),
    TWIGVALLEY_LEATHER(new Good().set(Items.LEATHER, TWIGVALLEY, FROM, null, 3, 4, NORMAL)),

    TWIGVALLEY_BLAZE_PAPRIKA(new Good().set(SPICE_BLAZE_PAPRIKA, TWIGVALLEY, FROM, null, 1, 2, RARE)),
    TWIGVALLEY_RED_SAND(new Good().set(Items.RED_SAND, TWIGVALLEY, FROM, null, 3, 5, RARE)),
    TWIGVALLEY_CUT_RED_SANDSTONE(new Good().set(Items.CUT_RED_SANDSTONE, TWIGVALLEY, FROM, null, 1, 2, RARE)),
    TWIGVALLEY_ROASTED_PECANS(new Good().set(SPICE_ROASTED_PECANS, TWIGVALLEY, FROM, null, 1, 2, RARE)),
    TWIGVALLEY_SWEET_BERRIES(new Good().set(Items.SWEET_BERRIES, TWIGVALLEY, FROM, null, 4, 5, RARE)),
    TWIGVALLEY_DRIED_SELAGINELLA(new Good().set(SPICE_DRIED_SELAGINELLA, TWIGVALLEY, FROM, null, 1, 2, RARE)),

    TWIGVALLEY_COCOA_BEANS(new Good().set(Items.COCOA_BEANS, TWIGVALLEY, FROM, null, 2, 2, SUPER_RARE)),
    TWIGVALLEY_GOLDEN_CARROT(new Good().set(Items.GOLDEN_CARROT, TWIGVALLEY, FROM, null, 2, 3, SUPER_RARE)),
    TWIGVALLEY_RED_TULIP(new Good().set(Items.RED_TULIP, TWIGVALLEY, FROM, WILL, 2, 3, SUPER_RARE)),
    TWIGVALLEY_ORANGE_TULIP(new Good().set(Items.ORANGE_TULIP, TWIGVALLEY, FROM, WILL, 2, 3, SUPER_RARE)),

    TWIGVALLEY_WITHER_ROSE(new Good().set(Items.WITHER_ROSE, TWIGVALLEY, FROM, ANU, 1, 1, UNIQUE)),
    TWIGVALLEY_GILDED_BLACKSTONE(new Good().set(Items.GILDED_BLACKSTONE, TWIGVALLEY, FROM, ANU, 3, 5, UNIQUE)),
    TWIGVALLEY_GOLDEN_HORSE_ARMOR(new Good().set(Items.GOLDEN_HORSE_ARMOR, TWIGVALLEY, FROM, null, 1, 1, UNIQUE)),
    TWIGVALLEY_GOLDEN_APPLE(new Good().set(Items.GOLDEN_APPLE, TWIGVALLEY, FROM, null, 1, 1, UNIQUE)),
    TWIGVALLEY_SOUL_LATERN(new Good().set(Items.SOUL_LANTERN, TWIGVALLEY, FROM, ANU, 1, 1, UNIQUE)),
    //To
    /*----------------------------------------------*/
    TWIGVALLEY_APPLE(new Good().set(Items.APPLE, TWIGVALLEY, TO, null, 1, 1, NORMAL)),
    TWIGVALLEY_FEATHER(new Good().set(Items.FEATHER, TWIGVALLEY, TO, ANU, 2, 3, NORMAL)),
    TWIGVALLEY_DIRT(new Good().set(Items.DIRT, TWIGVALLEY, TO, ANU, 3, 4, NORMAL)),

    TWIGVALLEY_OAK_LEAVES(new Good().set(Items.OAK_LEAVES, TWIGVALLEY, TO, ANU, 2, 3, RARE)),
    TWIGVALLEY_DARK_OAK_LEAVES(new Good().set(Items.DARK_OAK_LEAVES, TWIGVALLEY, TO, ANU, 2, 3, RARE));

    private final Good good;

    Goods(Good good) {
        this.good = good;
    }

    public Good getGood() {
        return good;
    }

    public static List<Good> getList() {
        return Arrays.stream(values()).map(good -> good.getGood()).collect(Collectors.toList());
    }
}
