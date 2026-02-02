package trepapto.taczreloaded.eject.mode;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import trepapto.taczreloaded.data.ModDataComponents;
import trepapto.taczreloaded.eject.EjectMode;
import trepapto.taczreloaded.eject.EjectionBehavior;
import trepapto.taczreloaded.eject.trigger.IOnEjectFinish;
import trepapto.taczreloaded.eject.trigger.IOnFireTrigger;
import trepapto.taczreloaded.eject.trigger.IOnGunDrawTrigger;
import trepapto.taczreloaded.eject.trigger.IOnReloadTrigger;

import java.util.Objects;

public class OnReloadEject extends EjectionBehavior 
    implements IOnFireTrigger, IOnReloadTrigger, IOnEjectFinish {

    private final int pendingShellCount;

    public OnReloadEject(ItemStack shell, float delay, int pendingShellCount) {
        super(shell, delay);
        this.pendingShellCount = pendingShellCount;
    }

    public OnReloadEject(ItemStack shell, float delay) {
        this(shell, delay, 0);
    }

    public static final MapCodec<OnReloadEject> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            ItemStack.CODEC.fieldOf("shell").forGetter(EjectionBehavior::getShell),
            Codec.FLOAT.optionalFieldOf("delay", 0f).forGetter(EjectionBehavior::getDelay),
            Codec.INT.optionalFieldOf("pendingShellCount", 0).forGetter(OnReloadEject::getPendingShellCount)
        ).apply(instance, OnReloadEject::new)
    );

    @Override
    public MapCodec<OnReloadEject> codec() {
        return CODEC;
    }

    @Override
    public EjectMode getMode() {
        return EjectMode.ON_RELOAD;
    }

    @Override
    public void onFire(LivingEntity shooter, ItemStack gunStack, int currentAmmo) {
        // 创建新实例，count + 1
        new OnReloadEject(shell, delay, pendingShellCount + 1).saveTo(gunStack);
    }

    @Override
    public void onReload(LivingEntity shooter, ItemStack gunStack, int currentAmmo) {
        if (pendingShellCount > 0) {
            ejectShellsWithCallback(shooter, gunStack, currentAmmo, pendingShellCount, getDelayTicks());
        }
    }

    @Override
    public void onEjectFinish(LivingEntity shooter, ItemStack gunStackSnapshot, ItemStack shellStack, int currentAmmo) {
        ItemStack actualGun = shooter.getMainHandItem();
        
        EjectionBehavior actualBehavior = actualGun.get(ModDataComponents.EJECTION_BEHAVIOR.get());
        if (actualBehavior instanceof OnReloadEject actual && actual.getPendingShellCount() > 0) {
            new OnReloadEject(shell, delay, 0).saveTo(actualGun);
        }
    }

    public int getPendingShellCount() {
        return pendingShellCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OnReloadEject that = (OnReloadEject) o;
        return pendingShellCount == that.pendingShellCount &&
               Float.compare(that.delay, delay) == 0 &&
               ItemStack.matches(shell, that.shell);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ItemStack.hashItemAndComponents(shell), delay, pendingShellCount);
    }
}