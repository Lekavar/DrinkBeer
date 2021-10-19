package lekavar.lma.drinkbeer;

import lekavar.lma.drinkbeer.block.BeerBarrelBlock;
import lekavar.lma.drinkbeer.block.BeerMugBlock;
import lekavar.lma.drinkbeer.block.BeerRecipeBoardBlock;
import lekavar.lma.drinkbeer.block.CallBellBlock;
import lekavar.lma.drinkbeer.block.entity.BeerBarrelEntity;
import lekavar.lma.drinkbeer.screen.BeerBarrelScreenHandler;
import lekavar.lma.drinkbeer.item.*;
import lekavar.lma.drinkbeer.statuseffects.DrunkFrostWalkerStatusEffect;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class DrinkBeer implements ModInitializer {
    public static DrinkBeer INSTANCE;

    public DrinkBeer() {
        INSTANCE = this;
    }

    public static final ItemGroup DRINK_BEER = FabricItemGroupBuilder.build(
            new Identifier("drinkbeer", "beer"),
            () -> new ItemStack(DrinkBeer.BEER_MUG));
    public static final ItemGroup DRINK_BEER_GENERAL = FabricItemGroupBuilder.build(
            new Identifier("drinkbeer", "general"),
            () -> new ItemStack(DrinkBeer.BEER_BARREL));

    //beer
    public static final Block BEER_MUG = new BeerMugBlock(FabricBlockSettings.of(Material.WOOD).hardness(1.0f));
    public static final Block BEER_MUG_BLAZE_STOUT = new BeerMugBlock(FabricBlockSettings.of(Material.WOOD).hardness(1.0f));
    public static final Block BEER_MUG_BLAZE_MILK_STOUT = new BeerMugBlock(FabricBlockSettings.of(Material.WOOD).hardness(1.0f));
    public static final Block BEER_MUG_APPLE_LAMBIC = new BeerMugBlock(FabricBlockSettings.of(Material.WOOD).hardness(1.0f));
    public static final Block BEER_MUG_SWEET_BERRY_KRIEK = new BeerMugBlock(FabricBlockSettings.of(Material.WOOD).hardness(1.0f));
    public static final Block BEER_MUG_HAARS_ICEY_PALE_LAGER = new BeerMugBlock(FabricBlockSettings.of(Material.WOOD).hardness(1.0f));
    public static final Block BEER_MUG_PUMPKIN_KVASS = new BeerMugBlock(FabricBlockSettings.of(Material.WOOD).hardness(1.0f));
    public static final Block BEER_MUG_FROTHY_PINK_EGGNOG = new BeerMugBlock(FabricBlockSettings.of(Material.WOOD).hardness(1.0f));

    //general
    public static final Block EMPTY_BEER_MUG = new BeerMugBlock(FabricBlockSettings.of(Material.WOOD).hardness(1.0f));

    public static BlockEntityType<BeerBarrelEntity> BEER_BARREL_ENTITY;
    public static final Block BEER_BARREL = new BeerBarrelBlock(FabricBlockSettings.of(Material.WOOD).hardness(2.0f));
    public static final Identifier BEER_BARREL_ID = new Identifier("drinkbeer", "beer_barrel");
    public static final ScreenHandlerType<BeerBarrelScreenHandler> BEER_BARREL_SCREEN_HANDLER;
    static {
        BEER_BARREL_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(BEER_BARREL_ID, BeerBarrelScreenHandler::new);
    }

    public static final Block IRON_CALL_BELL = new CallBellBlock(FabricBlockSettings.of(Material.METAL).hardness(1.0f));
    public static final Block GOLDEN_CALL_BELL = new CallBellBlock(FabricBlockSettings.of(Material.METAL).hardness(1.0f));

    public static final Block RECIPE_BOARD_BEER_MUG = new BeerRecipeBoardBlock(FabricBlockSettings.of(Material.WOOD).hardness(1.0f));
    public static final Block RECIPE_BOARD_BEER_MUG_BLAZE_STOUT = new BeerRecipeBoardBlock(FabricBlockSettings.of(Material.WOOD).hardness(1.0f));

    //sounds
    public static final Identifier DRINKING_BEER = new Identifier("drinkbeer:drinking_beer");
    public static SoundEvent DRINKING_BEER_EVENT = new SoundEvent(DRINKING_BEER);
    public static final Identifier POURING = new Identifier("drinkbeer:pouring");
    public static SoundEvent POURING_EVENT = new SoundEvent(POURING);
    public static final Identifier POURING_CHRISTMAS = new Identifier("drinkbeer:pouring_christmas");
    public static SoundEvent POURING_CHRISTMAS_EVENT = new SoundEvent(POURING_CHRISTMAS);
    public static final Identifier IRON_CALL_BELL_TINKLE = new Identifier("drinkbeer:iron_call_bell_tinkle");
    public static SoundEvent IRON_CALL_BELL_TINKLE_EVENT = new SoundEvent(IRON_CALL_BELL_TINKLE);
    public static final Identifier GOLDEN_CALL_BELL_TINKLE = new Identifier("drinkbeer:golden_call_bell_tinkle");
    public static SoundEvent GOLDEN_CALL_BELL_TINKLE_EVENT = new SoundEvent(GOLDEN_CALL_BELL_TINKLE);

    //status effects
    public static final StatusEffect DRUNK_FROST_WALKER = new DrunkFrostWalkerStatusEffect();

    @Override
    public void onInitialize() {
        //beer
        Registry.register(Registry.BLOCK, new Identifier("drinkbeer", "beer_mug"), BEER_MUG);
        Registry.register(Registry.ITEM, new Identifier("drinkbeer", "beer_mug"), new BeerMugBlockItem(BEER_MUG, new Item.Settings().group(DRINK_BEER).food(new FoodComponent.Builder().hunger(2).statusEffect(new StatusEffectInstance(StatusEffects.HASTE,1200),1).alwaysEdible().build()).maxCount(16)));
        Registry.register(Registry.BLOCK, new Identifier("drinkbeer", "beer_mug_blaze_stout"), BEER_MUG_BLAZE_STOUT);
        Registry.register(Registry.ITEM, new Identifier("drinkbeer", "beer_mug_blaze_stout"), new BeerMugBlockItem(BEER_MUG_BLAZE_STOUT, new Item.Settings().group(DRINK_BEER).food(new FoodComponent.Builder().hunger(2).statusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE,1800),1).alwaysEdible().build()).maxCount(16)));
        Registry.register(Registry.BLOCK, new Identifier("drinkbeer", "beer_mug_blaze_milk_stout"), BEER_MUG_BLAZE_MILK_STOUT);
        Registry.register(Registry.ITEM, new Identifier("drinkbeer", "beer_mug_blaze_milk_stout"), new BeerMugBlockItem(BEER_MUG_BLAZE_MILK_STOUT, new Item.Settings().group(DRINK_BEER).food(new FoodComponent.Builder().hunger(2).statusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE,2400),1).alwaysEdible().build()).maxCount(16)));
        Registry.register(Registry.BLOCK, new Identifier("drinkbeer", "beer_mug_apple_lambic"), BEER_MUG_APPLE_LAMBIC);
        Registry.register(Registry.ITEM, new Identifier("drinkbeer", "beer_mug_apple_lambic"), new BeerMugBlockItem(BEER_MUG_APPLE_LAMBIC, new Item.Settings().group(DRINK_BEER).food(new FoodComponent.Builder().hunger(3).statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION,300),1).alwaysEdible().build()).maxCount(16)));
        Registry.register(Registry.BLOCK, new Identifier("drinkbeer", "beer_mug_sweet_berry_kriek"), BEER_MUG_SWEET_BERRY_KRIEK);
        Registry.register(Registry.ITEM, new Identifier("drinkbeer", "beer_mug_sweet_berry_kriek"), new BeerMugBlockItem(BEER_MUG_SWEET_BERRY_KRIEK, new Item.Settings().group(DRINK_BEER).food(new FoodComponent.Builder().hunger(3).statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION,400),1).alwaysEdible().build()).maxCount(16)));
        Registry.register(Registry.BLOCK, new Identifier("drinkbeer", "beer_mug_haars_icey_pale_lager"), BEER_MUG_HAARS_ICEY_PALE_LAGER);
        Registry.register(Registry.ITEM, new Identifier("drinkbeer", "beer_mug_haars_icey_pale_lager"), new BeerMugBlockItem(BEER_MUG_HAARS_ICEY_PALE_LAGER, new Item.Settings().group(DRINK_BEER).food(new FoodComponent.Builder().hunger(1).statusEffect(new StatusEffectInstance(DrinkBeer.DRUNK_FROST_WALKER,1200),1).alwaysEdible().build()).maxCount(16)));
        Registry.register(Registry.BLOCK, new Identifier("drinkbeer", "beer_mug_pumpkin_kvass"), BEER_MUG_PUMPKIN_KVASS);
        Registry.register(Registry.ITEM, new Identifier("drinkbeer", "beer_mug_pumpkin_kvass"), new BeerMugBlockItem(BEER_MUG_PUMPKIN_KVASS, new Item.Settings().group(DRINK_BEER).food(new FoodComponent.Builder().hunger(9).alwaysEdible().build()).maxCount(16)));
        Registry.register(Registry.BLOCK, new Identifier("drinkbeer", "beer_mug_frothy_pink_eggnog"), BEER_MUG_FROTHY_PINK_EGGNOG);
        Registry.register(Registry.ITEM, new Identifier("drinkbeer", "beer_mug_frothy_pink_eggnog"), new BeerMugBlockItem(BEER_MUG_FROTHY_PINK_EGGNOG, new Item.Settings().group(DRINK_BEER).food(new FoodComponent.Builder().hunger(2).alwaysEdible().build()).maxCount(16)));

        //general
        Registry.register(Registry.BLOCK, new Identifier("drinkbeer", "empty_beer_mug"), EMPTY_BEER_MUG);
        Registry.register(Registry.ITEM, new Identifier("drinkbeer", "empty_beer_mug"), new BlockItem(EMPTY_BEER_MUG, new Item.Settings().group(DRINK_BEER_GENERAL).maxCount(16)));

        BEER_BARREL_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "drinkbeer:beer_barrel_entity", BlockEntityType.Builder.create(BeerBarrelEntity::new, BEER_BARREL).build(null));
        Registry.register(Registry.BLOCK, new Identifier("drinkbeer", "beer_barrel"), BEER_BARREL);
        Registry.register(Registry.ITEM, new Identifier("drinkbeer", "beer_barrel"), new BlockItem(BEER_BARREL, new Item.Settings().group(DRINK_BEER_GENERAL)));

        Registry.register(Registry.BLOCK, new Identifier("drinkbeer", "iron_call_bell"), IRON_CALL_BELL);
        Registry.register(Registry.ITEM, new Identifier("drinkbeer", "iron_call_bell"), new BlockItem(IRON_CALL_BELL, new Item.Settings().group(DRINK_BEER_GENERAL).maxCount(64)));
        Registry.register(Registry.BLOCK, new Identifier("drinkbeer", "golden_call_bell"), GOLDEN_CALL_BELL);
        Registry.register(Registry.ITEM, new Identifier("drinkbeer", "golden_call_bell"), new BlockItem(GOLDEN_CALL_BELL, new Item.Settings().group(DRINK_BEER_GENERAL).maxCount(64)));

        Registry.register(Registry.BLOCK, new Identifier("drinkbeer", "recipe_board_beer_mug"), RECIPE_BOARD_BEER_MUG);
        Registry.register(Registry.ITEM, new Identifier("drinkbeer", "recipe_board_beer_mug"), new BlockItem(RECIPE_BOARD_BEER_MUG, new Item.Settings().group(DRINK_BEER_GENERAL).maxCount(1)));
        Registry.register(Registry.BLOCK, new Identifier("drinkbeer", "recipe_board_beer_mug_blaze_stout"), RECIPE_BOARD_BEER_MUG_BLAZE_STOUT);
        Registry.register(Registry.ITEM, new Identifier("drinkbeer", "recipe_board_beer_mug_blaze_stout"), new BlockItem(RECIPE_BOARD_BEER_MUG_BLAZE_STOUT, new Item.Settings().group(DRINK_BEER_GENERAL).maxCount(1)));

        //sounds
        Registry.register(Registry.SOUND_EVENT, DrinkBeer.DRINKING_BEER, DRINKING_BEER_EVENT);
        Registry.register(Registry.SOUND_EVENT, DrinkBeer.POURING, POURING_EVENT);
        Registry.register(Registry.SOUND_EVENT, DrinkBeer.POURING_CHRISTMAS, POURING_CHRISTMAS_EVENT);
        Registry.register(Registry.SOUND_EVENT, DrinkBeer.IRON_CALL_BELL_TINKLE, IRON_CALL_BELL_TINKLE_EVENT);
        Registry.register(Registry.SOUND_EVENT, DrinkBeer.GOLDEN_CALL_BELL_TINKLE, GOLDEN_CALL_BELL_TINKLE_EVENT);

        //status effects
        Registry.register(Registry.STATUS_EFFECT, new Identifier("drinkbeer", "drunk_frost_walker"), DRUNK_FROST_WALKER);
    }
}
