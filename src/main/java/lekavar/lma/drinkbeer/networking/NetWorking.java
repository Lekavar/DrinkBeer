package lekavar.lma.drinkbeer.networking;

import io.netty.buffer.Unpooled;
import lekavar.lma.drinkbeer.block.entity.TradeboxEntity;
import lekavar.lma.drinkbeer.screen.TradeBoxScreenHandler;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.player.PlayerEntity;
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
        ServerSidePacketRegistry.INSTANCE.register(SEND_REFRESH_TRADEBOX, (packetContext, packetByteBuf)->{
            ScreenHandler screenHandler = packetContext.getPlayer().currentScreenHandler;
            if (screenHandler instanceof TradeBoxScreenHandler) {
                BlockPos pos = packetByteBuf.readBlockPos();
                PlayerEntity player = packetContext.getPlayer();
                packetContext.getTaskQueue().execute(() -> {
                    TradeboxEntity tradeboxEntity = (TradeboxEntity) player.world.getBlockEntity(pos);
                    tradeboxEntity.screenHandler.setTradeboxCooling();
                });
            }
        });
    }

    public static void sendRefreshTradebox(BlockPos pos) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeBlockPos(pos);
        ClientSidePacketRegistry.INSTANCE.sendToServer(SEND_REFRESH_TRADEBOX, buf);
    }
}
