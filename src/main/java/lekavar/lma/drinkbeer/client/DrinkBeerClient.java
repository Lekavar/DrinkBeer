package lekavar.lma.drinkbeer.client;

import lekavar.lma.drinkbeer.DrinkBeer;
import lekavar.lma.drinkbeer.manager.MixedBeerManager;
import lekavar.lma.drinkbeer.screen.BartendingTableScreen;
import lekavar.lma.drinkbeer.screen.BeerBarrelScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.particle.EmotionParticle;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class DrinkBeerClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        //Beer barrel's screen handler
        ScreenRegistry.register(DrinkBeer.BEER_BARREL_SCREEN_HANDLER, BeerBarrelScreen::new);
        //Bartending table's screen handler
        ScreenRegistry.register(DrinkBeer.BARTENDING_TABLE_SCREEN_HANDLER, BartendingTableScreen::new);
        //Mixed beer entity's renderer
        //TODO Q:实体渲染怎么注册
       //BlockEntityRendererRegistry.INSTANCE.register(DrinkBeer.MIXED_BEER_ENTITY, MixedBeerEntityRenderer::new);
        //Mixed beer's model predicate provider
        try {
            //TODO Q:为什么不能根据beerId变化item model
            FabricModelPredicateProviderRegistry.register(DrinkBeer.MIXED_BEER.asItem(), new Identifier("beer_id"), (stack, world, entity, seed)
                    -> MixedBeerManager.getBeerId(stack));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        //Particles
        ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).register(((atlasTexture, registry) -> {
            registry.register(new Identifier("drinkbeer", "particle/mixed_beer_default"));
            registry.register(new Identifier("drinkbeer", "particle/call_bell_tinkle_paw"));
        }));
        ParticleFactoryRegistry.getInstance().register(DrinkBeer.MIXED_BEER_DEFAULT, FlameParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(DrinkBeer.CALL_BELL_TINKLE_PAW, EmotionParticle.AngryVillagerFactory::new);
        /*Transparent texture*/
        //Bartending table
        BlockRenderLayerMap.INSTANCE.putBlock(DrinkBeer.BARTENDING_TABLE_NORMAL, RenderLayer.getTranslucent());
        //Spices
        BlockRenderLayerMap.INSTANCE.putBlock(DrinkBeer.SPICE_AMETHYST_NIGELLA_SEEDS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(DrinkBeer.SPICE_CITRINE_NIGELLA_SEEDS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(DrinkBeer.SPICE_STORM_SHARDS, RenderLayer.getTranslucent());
    }
}
