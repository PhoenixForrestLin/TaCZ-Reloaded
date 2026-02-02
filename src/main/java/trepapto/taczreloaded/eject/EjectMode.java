package trepapto.taczreloaded.eject;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.util.StringRepresentable;
import trepapto.taczreloaded.eject.mode.*;

public enum EjectMode implements StringRepresentable {
    ON_FIRE("on_fire", OnFireEject.CODEC),
    ON_RELOAD("on_reload", OnReloadEject.CODEC),
    HYBRID("hybrid", HybridEject.CODEC),
    NONE("none", NoEject.CODEC);

    private final String name;
    private final MapCodec<? extends EjectionBehavior> codec;

    EjectMode(String name, MapCodec<? extends EjectionBehavior> codec) {
        this.name = name;
        this.codec = codec;
    }

    @Override
    public String getSerializedName() {
        return name;
    }

    public MapCodec<? extends EjectionBehavior> codec() {
        return codec;
    }

    public static final Codec<EjectMode> CODEC = StringRepresentable.fromEnum(EjectMode::values);
}