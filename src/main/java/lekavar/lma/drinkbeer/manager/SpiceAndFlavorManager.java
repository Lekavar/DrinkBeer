package lekavar.lma.drinkbeer.manager;

import lekavar.lma.drinkbeer.util.mixedbeer.FlavorCombinations;
import lekavar.lma.drinkbeer.util.mixedbeer.Flavors;
import lekavar.lma.drinkbeer.util.mixedbeer.MixedBeerOnUsing;
import lekavar.lma.drinkbeer.util.mixedbeer.Spices;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SpiceAndFlavorManager {
    public static String getSpiceToolTipTranslationKey() {
        return "item.drinkbeer.spice.tooltip";
    }

    public static String getNoFlavorToolTipTranslationKey() {
        return "item.drinkbeer.spice.tooltip_no_flavor";
    }

    public static String getFlavorToolTipTranslationKey() {
        return "item.drinkbeer.spice.tooltip_flavor";
    }

    public static String getFlavorTranslationKey(int flavorId) {
        return getFlavorTranslationKey(Flavors.byId(flavorId));
    }

    public static String getFlavorTranslationKey(Flavors flavor) {
        return "drinkbeer.flavor." + flavor.getName();
    }

    public static String getFlavorToolTipTranslationKey(int flavorId) {
        return getFlavorTranslationKey(flavorId) + ".tooltip";
    }

    public static DefaultParticleType getLastSpiceFlavorParticle(List<Integer> spiceList) {
        if (spiceList != null) {
            if (!spiceList.isEmpty()) {
                int lastSpiceId = spiceList.get(spiceList.size() - 1);
                return Spices.byId(lastSpiceId).getFlavor().getParticle();
            }
        }
        return Flavors.DEFAULT_PARTICLE;
    }

    private static List<Flavors> spiceListToFlavorList(@NotNull List<Integer> spiceList) {
        List<Flavors> flavorList = new ArrayList<>();
        if (!spiceList.isEmpty()) {
            for (int spiceId : spiceList) {
                flavorList.add(Spices.byId(spiceId).getFlavor());
            }
        }
        return flavorList;
    }

    public static Flavors getCombinedFlavor(List<Integer> spiceList) {
        FlavorCombinations flavorCombination = matchFlavorCombination(spiceListToFlavorList(spiceList));
        return flavorCombination != null ? flavorCombination.getCombinedFlavor() : null;
    }

    private static FlavorCombinations matchFlavorCombination(List<Flavors> flavorList) {
        if (flavorList == null)
            return null;
        //Trans flavor to it's father flavor(if exists)
        for (int i = 0; i < flavorList.size(); i++) {
            Flavors fatherFlavor = flavorList.get(i).getFatherFlavor();
            if (fatherFlavor != null) {
                flavorList.set(i, fatherFlavor);
            }
        }
        for (FlavorCombinations flavorCombination : FlavorCombinations.getFlavorCombinationList()) {
            List<Pair<List<Flavors>, Boolean>> flavorCombinationList = flavorCombination.getFlavorCombination().getFlavorCombinationList();
            try {
                for (Pair<List<Flavors>, Boolean> flavorCombinationPair : flavorCombinationList) {
                    //Check flavors in order
                    if (flavorCombinationPair.getValue()) {
                        if (flavorCombinationPair.getKey().equals(flavorList)) {
                            return flavorCombination;
                        }
                    }
                    //Check flavors out of order
                    else {
                        List<Flavors> tempFlavorList1 = new ArrayList<>();
                        List<Flavors> tempFlavorList2 = new ArrayList<>();

                        Collections.copy(tempFlavorList1, flavorList);
                        Collections.copy(tempFlavorList2, flavorCombinationPair.getKey());

                        Collections.sort(tempFlavorList1);
                        Collections.sort(tempFlavorList2);
                        if (tempFlavorList1.equals(tempFlavorList2)) {
                            return flavorCombination;
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("NULL value of FlavorCombination in FlavorCombinations!");
                System.out.println("Found in " + flavorCombination.getCombinedFlavor());
            }
        }
        return null;
    }

    public static void applyFlavorValue(MixedBeerOnUsing mixedBeerOnUsing) {
        if (!mixedBeerOnUsing.getFlavorList().isEmpty()) {
            for (Flavors flavor : mixedBeerOnUsing.getFlavorList()) {
                switch (flavor) {
                    case SPICY, FIERY -> applySpicyFlavorValue(mixedBeerOnUsing, flavor);
                    case AROMITIC, AROMITIC1 -> applyAromiticFlavorValue(mixedBeerOnUsing, flavor);
                    case REFRESHING, REFRESHING1 -> applyRefreshingFlavorValue(mixedBeerOnUsing, flavor);
                    case NUTTY -> applyNuttyFlavorAction(mixedBeerOnUsing, flavor);
                    case SWEET -> applySweetFlavorAction(mixedBeerOnUsing, flavor);
                    case LUSCIOUS -> applySweetFlavorAction(mixedBeerOnUsing, flavor);
                    case CLOYING -> applySweetFlavorAction(mixedBeerOnUsing, flavor);
                    case NUTTY1 -> applyNuttyFlavorAction(mixedBeerOnUsing, flavor);
                    case MELLOW -> applyMellowFlavorAction(mixedBeerOnUsing, flavor);
                    default -> mixedBeerOnUsing.addAction(flavor);
                }
            }
        }
    }

    public static void applyFlavorAction(MixedBeerOnUsing mixedBeerOnUsing, World world, LivingEntity user) {
        List<Flavors> actionList = mixedBeerOnUsing.getActionList();
        if (!actionList.isEmpty()) {
            for (int i = 0; i < actionList.size(); i++) {
                Flavors flavor = actionList.get(i);
                switch (flavor) {
                    case SOOOOO_SPICY -> applySoooooSpicyFlavorAction(user);
                    case STORMY -> {
                        if (!MixedBeerManager.hasActionAfter(i, Flavors.STORMY, mixedBeerOnUsing.getActionList())
                                && !MixedBeerManager.hasActionAfter(i, Flavors.THE_FALL_OF_THE_GIANT, mixedBeerOnUsing.getActionList()))
                            applyStormyFlavorAction(MixedBeerManager.getActionedTimes(i, Flavors.STORMY, mixedBeerOnUsing.getActionList()), world, user);
                    }
                    case THE_FALL_OF_THE_GIANT -> applyStormyFlavorAction(3, world, user);
                    case DRYING -> {
                        if (!MixedBeerManager.hasActionAfter(i, Flavors.DRYING, mixedBeerOnUsing.getActionList())) {
                            applyDryingFlavorAction(MixedBeerManager.getActionedTimes(i, Flavors.DRYING, mixedBeerOnUsing.getActionList()), world, user);
                        }
                        break;
                    }
                }
            }
        }
    }

    public static void applySpicyFlavorValue(MixedBeerOnUsing mixedBeerOnUsing, Flavors flavor) {
        switch (flavor) {
            case SPICY -> {
                mixedBeerOnUsing.addHealth(-3);
                mixedBeerOnUsing.multiplyAllStatusEffectDuration(1.8f);
            }
            case FIERY -> {
                mixedBeerOnUsing.addHealth(-4);
                mixedBeerOnUsing.multiplyAllStatusEffectDuration(2f);
            }
        }
    }

    public static void applySoooooSpicyFlavorAction(LivingEntity user) {
        int fireTicks = 100;
        if (!user.isOnFire()) {
            user.setFireTicks(fireTicks);
        } else {
            user.setFireTicks(user.getFireTicks() + fireTicks);
        }
    }

    public static void applyAromiticFlavorValue(MixedBeerOnUsing mixedBeerOnUsing, Flavors flavor) {
        switch (flavor) {
            case AROMITIC -> mixedBeerOnUsing.addAllStatusEffectDuration(800);
            case AROMITIC1 -> mixedBeerOnUsing.addAllStatusEffectDuration(1200);
        }
    }

    public static void applyRefreshingFlavorValue(MixedBeerOnUsing mixedBeerOnUsing, Flavors flavor) {
        switch (flavor) {
            case REFRESHING -> mixedBeerOnUsing.addDrunkValue(-1);
            case REFRESHING1 -> mixedBeerOnUsing.addDrunkValue(-2);
        }
    }

    public static void applyStormyFlavorAction(int actionedTimes, World world, LivingEntity user) {
        int range = 9 + (actionedTimes == 3 ? 22 : actionedTimes * 4);
        int halfRange = (range - 1) / 2;
        int xStart = 0;
        int xEnd = 0;
        int zStart = 0;
        int zEnd = 0;
        Direction direction = user.getMovementDirection();

        switch (direction) {
            case NORTH -> {
                xStart = -halfRange;
                xEnd = halfRange + 1;
                zStart = -range;
            }
            case SOUTH -> {
                xStart = -halfRange;
                xEnd = halfRange + 1;
                zEnd = range;
            }
            case EAST -> {
                zStart = -halfRange;
                zEnd = halfRange + 1;
                xEnd = range;
            }
            case WEST -> {
                zStart = -halfRange;
                zEnd = halfRange + 1;
                xStart = -range;
            }
        }
        for (int x = xStart; x < xEnd; x++) {
            for (int y = 0; y < range; y++) {
                for (int z = zStart; z < zEnd; z++) {
                    BlockPos pos = user.getBlockPos().add(x, y, z);
                    BlockState blockState = world.getBlockState(pos);

                    if (blockState.isIn(BlockTags.LOGS) || blockState.isIn(BlockTags.LEAVES)) {
                        world.breakBlock(pos, true);
                    }
                }
            }
        }
    }

    public static void applyNuttyFlavorAction(MixedBeerOnUsing mixedBeerOnUsing, Flavors flavor) {
        switch (flavor) {
            case NUTTY:
                mixedBeerOnUsing.addHunger(4);
                break;
            case NUTTY1:
                mixedBeerOnUsing.addHunger(5);
                break;
        }
    }

    public static void applySweetFlavorAction(MixedBeerOnUsing mixedBeerOnUsing, Flavors flavor) {
        switch (flavor) {
            case SWEET:
                mixedBeerOnUsing.addHealth(3);
                break;
            case LUSCIOUS:
                mixedBeerOnUsing.addHealth(4);
                break;
            case CLOYING:
                mixedBeerOnUsing.addHealth(1);
        }
    }

    public static void applyMellowFlavorAction(MixedBeerOnUsing mixedBeerOnUsing, Flavors flavor) {
        switch (flavor) {
            case MELLOW:
                mixedBeerOnUsing.addSpecificStatusEffectDuration(StatusEffects.RESISTANCE, 1600);
                break;
        }
    }

    public static void applyDryingFlavorAction(int actionedTimes, World world, LivingEntity user) {
        int range = 17 + actionedTimes * 4;
        int halfRange = (range - 1) / 2;
        int xStart = 0;
        int xEnd = 0;
        int zStart = 0;
        int zEnd = 0;
        Direction direction = user.getMovementDirection();

        switch (direction) {
            case NORTH: {
                xStart = -halfRange;
                xEnd = halfRange + 1;
                zStart = -range;
                break;
            }
            case SOUTH: {
                xStart = -halfRange;
                xEnd = halfRange + 1;
                zEnd = range;
                break;
            }
            case EAST: {
                zStart = -halfRange;
                zEnd = halfRange + 1;
                xEnd = range;
                break;
            }
            case WEST: {
                zStart = -halfRange;
                zEnd = halfRange + 1;
                xStart = -range;
                break;
            }
        }
        for (int x = xStart; x < xEnd; x++) {
            for (int y = 0; y < range; y++) {
                for (int z = zStart; z < zEnd; z++) {
                    BlockPos pos = user.getBlockPos().add(x, y, z);
                    BlockState blockState = world.getBlockState(pos);
                    if (blockState.getBlock().equals(Blocks.WATER)) {
                        world.setBlockState(pos, Blocks.AIR.getDefaultState());
                    }
                }
            }
        }
    }
}
