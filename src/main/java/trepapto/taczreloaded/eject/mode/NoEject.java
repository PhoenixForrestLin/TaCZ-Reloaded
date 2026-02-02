package trepapto.taczreloaded.eject.mode;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.item.ItemStack;
import trepapto.taczreloaded.eject.EjectMode;
import trepapto.taczreloaded.eject.EjectionBehavior;

public class NoEject extends EjectionBehavior {

    public static final NoEject INSTANCE = new NoEject();

    private NoEject() {
        super(ItemStack.EMPTY, 0f);
    }

    public static final MapCodec<NoEject> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public MapCodec<NoEject> codec() {
        return CODEC;
    }

    @Override
    public EjectMode getMode() {
        return EjectMode.NONE;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof NoEject;
    }

    @Override
    public int hashCode() {
        return NoEject.class.hashCode();
    }
}