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

import java.util.Objects;

public class OnFireEject extends EjectionBehavior 
    implements IOnFireTrigger, IOnGunDrawTrigger, IOnEjectFinish {

    private final boolean pendingShell;

    public OnFireEject(ItemStack shell, float delay, boolean pendingShell) {
        super(shell, delay);
        this.pendingShell = pendingShell;
    }

    public static final MapCodec<OnFireEject> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            ItemStack.CODEC.fieldOf("shell").forGetter(EjectionBehavior::getShell),
            Codec.FLOAT.optionalFieldOf("delay", 0f).forGetter(EjectionBehavior::getDelay),
            Codec.BOOL.optionalFieldOf("pendingShell", false).forGetter(OnFireEject::isPendingShell)
        ).apply(instance, OnFireEject::new)
    );

    @Override
    public MapCodec<OnFireEject> codec() {
        return CODEC;
    }

    @Override
    public EjectMode getMode() {
        return EjectMode.ON_FIRE;
    }

    @Override
    public void onFire(LivingEntity shooter, ItemStack gunStack, int currentAmmo) {
        // 创建新实例，pendingShell = true
        new OnFireEject(shell, delay, true).saveTo(gunStack);
        ejectShellWithCallback(shooter, gunStack, currentAmmo, getDelayTicks());
    }

    @Override
    public void onGunDraw(LivingEntity shooter, ItemStack currentGunStack, int currentAmmo) {
        if (pendingShell) {
            // 保持延迟，传入回调
            ejectShellWithCallback(shooter, currentGunStack, currentAmmo, getDelayTicks());
        }
    }

    @Override
    public void onEjectFinish(LivingEntity shooter, ItemStack gunStackSnapshot, ItemStack shellStack, int currentAmmo) {
        // 从玩家手里获取实际的枪
        ItemStack actualGun = shooter.getMainHandItem();
        
        // 验证是否是同一把枪（通过比较 behavior）
        EjectionBehavior actualBehavior = actualGun.get(ModDataComponents.EJECTION_BEHAVIOR.get());
        if (actualBehavior instanceof OnFireEject actual && actual.isPendingShell()) {
            new OnFireEject(shell, delay, false).saveTo(actualGun);
        }
    }

    public boolean isPendingShell() {
        return pendingShell;
    }

    // ========== equals & hashCode ==========

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OnFireEject that = (OnFireEject) o;
        return pendingShell == that.pendingShell &&
               Float.compare(that.delay, delay) == 0 &&
               ItemStack.matches(shell, that.shell);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ItemStack.hashItemAndComponents(shell), delay, pendingShell);
    }
}