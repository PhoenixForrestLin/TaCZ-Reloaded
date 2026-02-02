// ShellSyncPayload.java
package trepapto.taczreloaded.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import trepapto.taczreloaded.TaCZReloaded;

public record ShellSyncPayload(
    int pendingCount,
    boolean hasPendingShell  // for OnFireEject
) implements CustomPacketPayload {

    public static final Type<ShellSyncPayload> TYPE = 
        new Type<>(ResourceLocation.fromNamespaceAndPath(TaCZReloaded.MODID, "shell_sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ShellSyncPayload> STREAM_CODEC =
        StreamCodec.composite(
            ByteBufCodecs.INT, ShellSyncPayload::pendingCount,
            ByteBufCodecs.BOOL, ShellSyncPayload::hasPendingShell,
            ShellSyncPayload::new
        );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}