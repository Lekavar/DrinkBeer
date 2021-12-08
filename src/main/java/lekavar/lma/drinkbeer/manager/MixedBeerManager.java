package lekavar.lma.drinkbeer.manager;

import com.mojang.datafixers.util.Pair;
import lekavar.lma.drinkbeer.DrinkBeer;
import lekavar.lma.drinkbeer.entity.damage.AlcoholDamage;
import lekavar.lma.drinkbeer.item.MixedBeerBlockItem;
import lekavar.lma.drinkbeer.statuseffects.DrunkStatusEffect;
import lekavar.lma.drinkbeer.statuseffects.NightHowlStatusEffect;
import lekavar.lma.drinkbeer.util.beer.Beers;
import lekavar.lma.drinkbeer.util.mixedbeer.Flavors;
import lekavar.lma.drinkbeer.util.mixedbeer.MixedBeerOnUsing;
import lekavar.lma.drinkbeer.util.mixedbeer.Spices;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MixedBeerManager {
    public static final int MAX_SPICES_COUNT = 3;

    public static ItemStack genMixedBeerItemStack(int beerId, int... spiceIds) {
        List<Integer> spiceList = new ArrayList<>();
        for (int spiceId : spiceIds) {
            spiceList.add(spiceId);
        }
        return genMixedBeerItemStack(beerId, spiceList);
    }

    public static ItemStack genMixedBeerItemStack(int beerId, List<Integer> spiceList) {
        ItemStack resultStack = new ItemStack(DrinkBeer.MIXED_BEER.asItem(), 1);

        spiceList = removeIllegalSpiceId(spiceList);

        NbtCompound compoundTag = resultStack.getOrCreateSubNbt("BlockEntityTag");
        NbtCompound tag = new NbtCompound();
        compoundTag.put("MixedBeer", tag);
        tag.putInt("BeerId", beerId);

        NbtList listTag = genSpiceListTag(spiceList);
        if (listTag != null) {
            tag.put("Spices", listTag);
        }

        resultStack.writeNbt(tag);
        return resultStack;
    }

    public static NbtList genSpiceListTag(List<Integer> spiceList) {
        NbtList listTag = null;
        if (!spiceList.isEmpty()) {
            listTag = new NbtList();
            for (int i = 0; i < MixedBeerManager.MAX_SPICES_COUNT && i < spiceList.size(); i++) {
                NbtCompound tag2 = new NbtCompound();
                tag2.putInt("SpiceId", spiceList.get(i));
                listTag.add(tag2);
            }
        }
        return listTag;
    }

    private static List<Integer> removeIllegalSpiceId(List<Integer> spiceList) {
        return spiceList.stream()
                .filter(value -> (value.compareTo(Spices.EMPTY_SPICE_ID) > 0) && (value.compareTo(Spices.size()) <= 0))
                .collect(Collectors.toList());
    }

    public static int getBeerId(ItemStack itemStack) {
        int beerId = Beers.EMPTY_BEER_ID;
        if (itemStack.getItem() instanceof MixedBeerBlockItem) {
            NbtCompound compoundTag = itemStack.getSubNbt("BlockEntityTag");
            if (compoundTag != null && compoundTag.contains("MixedBeer")) {
                NbtCompound tag = compoundTag.getCompound("MixedBeer");
                beerId = tag.getInt("BeerId");
            }
        }

        return beerId;
    }

    public static List<Integer> getSpiceList(ItemStack itemStack) {
        List<Integer> spiceList = new ArrayList<>();
        NbtCompound compoundTag = itemStack.getSubNbt("BlockEntityTag");
        if (compoundTag != null && compoundTag.contains("MixedBeer")) {
            NbtCompound tag = compoundTag.getCompound("MixedBeer");
            NbtList listTag = tag.getList("Spices", 10);
            for (int i = 0; i < listTag.size() && i < MixedBeerManager.MAX_SPICES_COUNT; ++i) {
                NbtCompound tag2 = listTag.getCompound(i);
                int spiceId = tag2.getInt("SpiceId");
                spiceList.add(spiceId);
            }
        }
        return spiceList;
    }

    public static String getMixedBeerTranslationKey() {
        return DrinkBeer.MIXED_BEER.asItem().getTranslationKey();
    }

    public static String getBaseBeerToolTipTranslationKey() {
        return "item.drinkbeer.mixed_beer.tooltip_base";
    }

    public static String getUnmixedToolTipTranslationKey() {
        return "item.drinkbeer.mixed_beer.tooltip_unmixed";
    }

    public static void useMixedBeer(ItemStack stack, World world, LivingEntity user) {
        /*Initialize properties!*/
        /*------------------------------------------------------------------------------------------------------------------*/
        MixedBeerOnUsing mixedBeerOnUsing = new MixedBeerOnUsing();
        //Initialize beer
        mixedBeerOnUsing.setBeer(Beers.byId(getBeerId(stack)));
        //Initialize food level
        mixedBeerOnUsing.addHunger(Objects.requireNonNull(mixedBeerOnUsing.getBeerItem().getFoodComponent()).getHunger());
        //Initialize spices and flavors
        List<Integer> spiceList = getSpiceList(stack);
        mixedBeerOnUsing.setSpiceList(spiceList);
        Flavors combinedFlavor = SpiceAndFlavorManager.getCombinedFlavor(spiceList);
        if (combinedFlavor != null) {
            mixedBeerOnUsing.addFlavor(combinedFlavor);
        }

        /*Deal with properties!*/
        /*------------------------------------------------------------------------------------------------------------------*/
        //Add base beer status effect
        mixedBeerOnUsing.addStatusEffect(getBeerStatusEffectList(mixedBeerOnUsing.getBeerItem(), world));
        //Deal with flavors
        SpiceAndFlavorManager.applyFlavorValue(mixedBeerOnUsing);

        /*Apply properties!*/
        /*------------------------------------------------------------------------------------------------------------------*/
        //Apply Drunk status effect
        DrunkStatusEffect.addStatusEffect(user, mixedBeerOnUsing.getDrunkValue());
        //Apply beer's special actions
        if (mixedBeerOnUsing.getBeer().equals(Beers.BEER_MUG_NIGHT_HOWL_KVASS)) {
            NightHowlStatusEffect.playRandomHowlSound(world, user);
        }
        //Apply food level
        if (user instanceof PlayerEntity && !((PlayerEntity) user).isCreative()) {
            ((PlayerEntity) user).getHungerManager().add(mixedBeerOnUsing.getHunger(), 0f);
        }
        //Apply health
        if (user instanceof PlayerEntity) {
            if (!((PlayerEntity) user).isCreative()) {
                if (mixedBeerOnUsing.getHealth() < 0) {
                    user.damage(new AlcoholDamage(), Math.abs(mixedBeerOnUsing.getHealth()));
                } else {
                    user.heal(mixedBeerOnUsing.getHealth());
                }
            }
        } else {
            user.setHealth(user.getHealth() + mixedBeerOnUsing.getHealth());
        }
        //Apply status effects
        for (org.apache.commons.lang3.tuple.Pair<StatusEffect, Integer> statusEffectPair : mixedBeerOnUsing.getStatusEffectList()) {
            user.addStatusEffect(new StatusEffectInstance(statusEffectPair.getKey(), statusEffectPair.getValue()));
        }
        //Apply flavor actions
        SpiceAndFlavorManager.applyFlavorAction(mixedBeerOnUsing, world, user);
    }

    private static List<org.apache.commons.lang3.tuple.Pair<StatusEffect, Integer>> getBeerStatusEffectList(Item beerItem, World world) {
        List<org.apache.commons.lang3.tuple.Pair<StatusEffect, Integer>> resultStatusEffectList = new ArrayList<>();
        List<Pair<StatusEffectInstance, Float>> statusEffectList = Objects.requireNonNull(beerItem.getFoodComponent()).getStatusEffects();
        if (statusEffectList != null) {
            if (!statusEffectList.isEmpty()) {
                for (Pair<StatusEffectInstance, Float> statusEffect : statusEffectList)
                    resultStatusEffectList.add(org.apache.commons.lang3.tuple.Pair.of(statusEffect.getFirst().getEffectType(), statusEffect.getFirst().getDuration()));
            }
        }
        if (beerItem.equals(Beers.BEER_MUG_NIGHT_HOWL_KVASS.getBeerItem())) {
            org.apache.commons.lang3.tuple.Pair<StatusEffect, Integer> nightHowlStatusEffectPair = NightHowlStatusEffect.getStatusEffectPair(world);
            resultStatusEffectList.add(nightHowlStatusEffectPair);
        }
        return resultStatusEffectList;
    }

    /**
     * Get the number of the target action occurrences before current action.
     *
     * @param index        Current action's index in actionList
     * @param targetAction Which action to find
     * @param actionList   Current mixed beer's actionList
     * @return Number of the target action occurrences before current action
     */
    public static int getActionedTimes(int index, Flavors targetAction, List<Flavors> actionList) {
        if (index == 0)
            return 0;
        int actionTime = 0;
        for (int i = 0; i < index; i++) {
            if (actionList.get(i).equals(targetAction)) {
                actionTime++;
            }
        }
        return actionTime;
    }

    /**
     * Whether the target action exists before the current action.
     *
     * @param index        Current action's index in actionList
     * @param targetAction Which action to find
     * @param actionList   Current mixed beer's actionList
     * @return
     */
    public static boolean hasActionedBefore(int index, Flavors targetAction, List<Flavors> actionList) {
        return getActionedTimes(index, targetAction, actionList) != 0;
    }

    /**
     * Whether the target action exists after current action.
     *
     * @param index        Current action's index in actionList
     * @param targetAction Which action to find
     * @param actionList   Current mixed beer's actionList
     * @return
     */
    public static boolean hasActionAfter(int index, Flavors targetAction, List<Flavors> actionList) {
        if (actionList.size() - 1 == index) {
            return false;
        } else {
            for (int i = index + 1; i < actionList.size(); i++) {
                if (actionList.get(i).equals(targetAction)) {
                    return true;
                }
            }
        }
        return false;
    }
}
