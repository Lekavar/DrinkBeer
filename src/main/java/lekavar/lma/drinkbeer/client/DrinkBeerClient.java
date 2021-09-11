package lekavar.lma.drinkbeer.client;

import lekavar.lma.drinkbeer.DrinkBeer;
import lekavar.lma.drinkbeer.screen.BeerBarrelScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

@Environment(EnvType.CLIENT)
public class DrinkBeerClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ScreenRegistry.register(DrinkBeer.BEER_BARREL_SCREEN_HANDLER, BeerBarrelScreen::new);
    }
}
