// ModNetwork.java
package trepapto.taczreloaded.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class ModNetwork {

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        registrar.playBidirectional(
            ShellSyncPayload.TYPE,
            ShellSyncPayload.STREAM_CODEC,
            new DirectionalPayloadHandler<>(
                ModNetwork::handleClientSync,
                ModNetwork::handleServerSync
            )
        );
    }

    private static void handleClientSync(ShellSyncPayload payload, 
            net.neoforged.neoforge.network.handling.IPayloadContext context) {
        // 客户端接收服务器同步的数据
        context.enqueueWork(() -> {
            // 更新本地 behavior 状态
        });
    }

    private static void handleServerSync(ShellSyncPayload payload,
            net.neoforged.neoforge.network.handling.IPayloadContext context) {
        // 服务器接收客户端的数据（如果需要）
        context.enqueueWork(() -> {
            // 验证并更新服务器状态
        });
    }
}