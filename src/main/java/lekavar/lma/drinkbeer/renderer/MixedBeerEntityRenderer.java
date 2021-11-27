package lekavar.lma.drinkbeer.renderer;

import lekavar.lma.drinkbeer.DrinkBeer;
import lekavar.lma.drinkbeer.block.entity.MixedBeerEntity;
import lekavar.lma.drinkbeer.util.beer.Beers;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3f;

public class MixedBeerEntityRenderer implements BlockEntityRenderer<MixedBeerEntity> {

    @Override
    public void render(MixedBeerEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();

        int beerId = blockEntity.getBeerId();
        ItemStack beerStack = getBeerStack(beerId);
        BlockPos pos = blockEntity.getPos();

        //Move beer
        matrices.translate(0.5, 0.25, 0.5);
        //Rotate beer
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(getRandomAngleByPos(pos)));
        //Get light at the beer block
        int lightAbove = WorldRenderer.getLightmapCoordinates(blockEntity.getWorld(), pos);
        //Render beer
        MinecraftClient.getInstance().getItemRenderer().renderItem(beerStack, ModelTransformation.Mode.GROUND ,lightAbove, overlay,matrices,vertexConsumers,0);

        matrices.pop();
    }

    private ItemStack getBeerStack(int beerId) {
        ItemStack itemStack;
        if (beerId > Beers.EMPTY_BEER_ID) {
            Beers beer = Beers.byId(beerId);
            Item item = beer.getBeerItem();
            itemStack = new ItemStack(item, 1);
        } else {
            itemStack = new ItemStack(DrinkBeer.MIXED_BEER.asItem(), 1);
        }
        return itemStack;
    }

    private float getRandomAngleByPos(BlockPos pos) {
        float angle = 0f;
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        int sum = Math.abs(x) + Math.abs(z) + Math.abs(y);
        angle = 360 * ((float) sum % 8 / 8);

        return angle;
    }
}