package trepapto.taczreloaded.data;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import trepapto.taczreloaded.eject.EjectionBehavior;
import static trepapto.taczreloaded.TaCZReloaded.MODID;

public class ModDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, MODID);

    public static final Supplier<DataComponentType<String>> SHELL_ID =
            DATA_COMPONENTS.register("shell_id", () ->
                    DataComponentType.<String>builder()
                            .persistent(Codec.STRING)
                            .networkSynchronized(ByteBufCodecs.STRING_UTF8)
            .build()
            );

    // EjectionBehavior 数据组件
    public static final Supplier<DataComponentType<EjectionBehavior>> EJECTION_BEHAVIOR =
            DATA_COMPONENTS.register("ejection_behavior", () ->
                    DataComponentType.<EjectionBehavior>builder()
                            .persistent(EjectionBehavior.CODEC)
                            .networkSynchronized(ByteBufCodecs.fromCodec(EjectionBehavior.CODEC))
                            .build()
            );

        
}
