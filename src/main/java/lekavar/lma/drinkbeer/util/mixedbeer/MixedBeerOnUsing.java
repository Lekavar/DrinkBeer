package lekavar.lma.drinkbeer.util.mixedbeer;

import lekavar.lma.drinkbeer.util.beer.Beers;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class MixedBeerOnUsing {
    private Beers beer;
    private int hunger;
    private float health;
    /**
     * drunkValue is NOT value of Drunk amplifier
     * It's value of the Drunk amplifier's change
     */
    private int drunkValue;
    private List<Spices> spiceList;
    private List<Flavors> flavorList;
    List<Pair<StatusEffect, Integer>> statusEffectList;
    List<Flavors> actionList;

    public MixedBeerOnUsing() {
        this.beer = null;
        this.hunger = 0;
        this.health = 0;
        //this.drunkValue = 1 because beers always have lv.1 Drunk status effect.
        this.drunkValue = 1;
        this.spiceList = new ArrayList<>();
        this.flavorList = new ArrayList<>();
        this.statusEffectList = new ArrayList<>();
        this.actionList = new ArrayList<>();
    }

    public Item getBeerItem() {
        return this.beer == null ? Beers.DEFAULT_BEER.getBeerItem() : this.beer.getBeerItem();
    }

    public Beers getBeer() {
        return beer;
    }

    public int getDrunkValue() {
        return drunkValue;
    }

    public int getHunger() {
        return hunger;
    }

    public float getHealth() {
        return health;
    }

    public List<Spices> getSpiceList() {
        return spiceList;
    }

    public List<Flavors> getFlavorList() {
        return flavorList;
    }

    public List<Pair<StatusEffect, Integer>> getStatusEffectList() {
        return statusEffectList;
    }

    public List<Flavors> getActionList() {
        return actionList;
    }

    public void setBeer(Beers beer) {
        this.beer = beer;
    }

    private void setHunger(int hunger) {
        this.hunger = hunger;
    }

    public void addHunger(int hunger) {
        setHunger(this.hunger + hunger);
    }

    private void setHealth(float health) {
        this.health = health;
    }

    public void addHealth(float health) {
        setHealth(this.health + health);
    }

    public void setSpiceList(List<Integer> spiceList) {
        for (int spiceId : spiceList) {
            Spices spice = Spices.byId(spiceId);
            this.spiceList.add(spice);
            this.flavorList.add(spice.getFlavor());
        }
    }

    private void setDrunkValue(int drunkValue) {
        this.drunkValue = drunkValue;
    }

    /**
     * @param drunkValue It is NOT value of Drunk amplifier, it's value of the Drunk amplifier's change.
     */
    public void addDrunkValue(int drunkValue) {
        setDrunkValue(this.drunkValue + drunkValue);
    }

    public void addFlavor(Flavors flavor) {
        this.flavorList.add(flavor);
    }

    public void addAction(Flavors flavor) {
        this.actionList.add(flavor);
    }

    public void addStatusEffect(List<Pair<StatusEffect, Integer>> newStatusEffectList) {
        for (Pair<StatusEffect, Integer> newStatusEffect : newStatusEffectList) {
            addSpecificStatusEffectDuration(newStatusEffect.getKey(), newStatusEffect.getValue());
        }
    }

    /**
     * Add targetStatusEffect into statusEffectList if targetStatusEffect doesn't exist in current statusEffectList.
     * Extend(or reduce) targetStatusEffect's duration if targetStatusEffect exists in current statusEffectList.
     * @param targetStatusEffect
     * @param duration
     */
    public void addSpecificStatusEffectDuration(StatusEffect targetStatusEffect, int duration) {
        if (!this.statusEffectList.isEmpty()) {
            for (int i=0;i<this.statusEffectList.size();i++) {
                Pair<StatusEffect, Integer> statusEffect = statusEffectList.get(i);
                if (statusEffect.getKey().equals(targetStatusEffect)) {
                    int newDuratin = (int) (statusEffect.getValue() + duration);
                    this.statusEffectList.set(i, Pair.of(statusEffect.getKey(), newDuratin));
                    return;
                }
            }
        }
        Pair<StatusEffect, Integer> statusEffect = Pair.of(targetStatusEffect, duration);
        this.statusEffectList.add(statusEffect);
    }

    public void addAllStatusEffectDuration(int duration) {
        if (!this.statusEffectList.isEmpty()) {
            for (int i=0;i<this.statusEffectList.size();i++) {
                Pair<StatusEffect, Integer> statusEffect = statusEffectList.get(i);
                int newDuratin = (int)(statusEffect.getValue() + duration);
                this.statusEffectList.set(i,Pair.of(statusEffect.getKey(),newDuratin));
            }
        }
    }

    /**
     * Find specific status effect and multiply it's duration by multiplyValue.
     * This only works when the targetStatusEffect exists in statusEffectList.
     * @param targetStatusEffect
     * @param multiplyValue
     * @return
     */
    public boolean multiplySpecificStatusEffectDuration(StatusEffect targetStatusEffect, float multiplyValue) {
        if (!this.statusEffectList.isEmpty()) {
            for (int i=0;i<this.statusEffectList.size();i++) {
                Pair<StatusEffect, Integer> statusEffect = statusEffectList.get(i);
                if (statusEffect.getKey().equals(targetStatusEffect)) {
                    int newDuration = (int) (statusEffect.getValue() * multiplyValue);
                    this.statusEffectList.set(i, Pair.of(statusEffect.getKey(), newDuration));
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Multiply all durations in statusEffectList by multiplyValue.
     * @param multiplyValue
     * @return
     */
    public void multiplyAllStatusEffectDuration(float multiplyValue) {
        if (!this.statusEffectList.isEmpty()) {
            for (int i=0;i<this.statusEffectList.size();i++) {
                Pair<StatusEffect, Integer> statusEffect = statusEffectList.get(i);
                int newDuration = (int)(statusEffect.getValue() * multiplyValue);
                this.statusEffectList.set(i,Pair.of(statusEffect.getKey(),newDuration));
            }
        }
    }
}
