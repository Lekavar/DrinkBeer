package lekavar.lma.drinkbeer.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.awt.*;

public class BeerBarrelScreen extends HandledScreen<ScreenHandler> {
    private static final Identifier BEER_BARREL_GUI_BASIC = new Identifier("drinkbeer", "textures/gui/container/beer_barrel.png");
    BeerBarrelScreenHandler screenHandler;

    public BeerBarrelScreen(ScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        screenHandler = (BeerBarrelScreenHandler) handler;
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BEER_BARREL_GUI_BASIC);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
        String str = screenHandler.propertyDelegate.get(3) == 1 ? convertTickToTime(screenHandler.getRemainingBrewingTime()) : convertTickToTime(screenHandler.getCurrentBrewingTime());
        textRenderer.draw(matrices, str, x + 128 - str.length() / 2, y + 54, new Color(64, 64, 64, 255).getRGB());
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    public String convertTickToTime(int tick) {
        String result;
        if (tick > 0) {
            double time = (double)tick / 20;
            int m = (int) (time / 60);
            int s = (int) (time % 60);
            result = m + ":" + s;
        } else result = "";
        return result;
    }

    @Override
    protected void init() {
        super.init();
        // Center the title
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
    }
}
