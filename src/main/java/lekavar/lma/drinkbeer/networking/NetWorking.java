package lekavar.lma.drinkbeer.networking;

import io.netty.buffer.Unpooled;
import lekavar.lma.drinkbeer.block.entity.TradeboxEntity;
import lekavar.lma.drinkbeer.screen.TradeBoxScreenHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class NetWorking {
    public static final Identifier SEND_REFRESH_TRADEBOX = new Identifier("drinkbeer", "snd_reftb");

    public static void init(){
        initServer();
    }

    public static void initServer(){
        ServerPlayNetworking.registerGlobalReceiver(SEND_REFRESH_TRADEBOX, (server, player, handler, buf, responseSender)->{
            ScreenHandler screenHandler = player.currentScreenHandler;
            if (screenHandler instanceof TradeBoxScreenHandler) {
                BlockPos pos = buf.readBlockPos();
                server.execute(() -> {
                    TradeboxEntity tradeboxEntity = (TradeboxEntity) player.world.getBlockEntity(pos);
                    tradeboxEntity.screenHandler.setTradeboxCooling();
                });
            }
        });
    }

    public static void sendRefreshTradebox(BlockPos pos) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeBlockPos(pos);
        ClientPlayNetworking.send(SEND_REFRESH_TRADEBOX, buf);
    }
}
