package trepapto.taczreloaded.eject.mode;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import trepapto.taczreloaded.data.ModDataComponents;
import trepapto.taczreloaded.eject.EjectMode;
import trepapto.taczreloaded.eject.EjectionBehavior;
import trepapto.taczreloaded.eject.trigger.IOnEjectFinish;
import trepapto.taczreloaded.eject.trigger.IOnFireTrigger;
import trepapto.taczreloaded.eject.trigger.IOnGunDrawTrigger;
import trepapto.taczreloaded.eject.trigger.IOnReloadTrigger;

import static trepapto.taczreloaded.TaCZReloaded.LOGGER;

import java.util.Objects;

public class HybridEject extends EjectionBehavior
    implements IOnFireTrigger, IOnReloadTrigger, IOnGunDrawTrigger, IOnEjectFinish {

    private final float reloadDelay;
    private final boolean lastShellPending;

    public HybridEject(ItemStack shell, float fireDelay, float reloadDelay, boolean lastShellPending) {
        super(shell, fireDelay);
        this.reloadDelay = reloadDelay;
        this.lastShellPending = lastShellPending;
    }

    public HybridEject(ItemStack shell, float fireDelay, float reloadDelay) {
        this(shell, fireDelay, reloadDelay, false);
    }

    public static final MapCodec<HybridEject> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            ItemStack.CODEC.fieldOf("shell").forGetter(EjectionBehavior::getShell),
            Codec.FLOAT.optionalFieldOf("fireDelay", 0f).forGetter(EjectionBehavior::getDelay),
            Codec.FLOAT.optionalFieldOf("reloadDelay", 0f).forGetter(HybridEject::getReloadDelay),
            Codec.BOOL.optionalFieldOf("lastShellPending", false).forGetter(HybridEject::isLastShellPending)
        ).apply(instance, HybridEject::new)
    );

    @Override
    public MapCodec<HybridEject> codec() {
        return CODEC;
    }

    @Override
    public EjectMode getMode() {
        return EjectMode.HYBRID;
    }

    @Override
    public void onFire(LivingEntity shooter, ItemStack gunStack, int currentAmmo) {
        
        if (currentAmmo>0) {
            // 正常抛壳
            ejectShellWithCallback(shooter, gunStack, currentAmmo, getDelayTicks());
        }
        new HybridEject(shell, delay, reloadDelay, true).saveTo(gunStack);
    }

    @Override
    public void onReload(LivingEntity shooter, ItemStack gunStack, int currentAmmo) {
        if (lastShellPending && !getHasBulletInBarrelFromStack(gunStack)) {
            ejectShellWithCallback(shooter, gunStack, currentAmmo, getReloadDelayTicks());
        }
    }

    @Override
    public void onGunDraw(LivingEntity shooter, ItemStack currentGunStack, int currentAmmo) {
        if (lastShellPending && currentAmmo>0) {
            ejectShellWithCallback(shooter, currentGunStack, currentAmmo, getDelayTicks());
        }
    }

    @Override
    public void onEjectFinish(LivingEntity shooter, ItemStack gunStackSnapshot, ItemStack shellStack, int currentAmmo) {
        ItemStack actualGun = shooter.getMainHandItem();
        
        EjectionBehavior actualBehavior = actualGun.get(ModDataComponents.EJECTION_BEHAVIOR.get());
        if (actualBehavior instanceof HybridEject actual && actual.isLastShellPending()) {
            new HybridEject(shell, delay, reloadDelay, false).saveTo(actualGun);
        }
    }

    public boolean isLastShellPending() {
        return lastShellPending;
    }

    public float getReloadDelay() {
        return reloadDelay;
    }

    public int getReloadDelayTicks() {
        return Math.round(reloadDelay * 20f);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HybridEject that = (HybridEject) o;
        return lastShellPending == that.lastShellPending &&
               Float.compare(that.delay, delay) == 0 &&
               Float.compare(that.reloadDelay, reloadDelay) == 0 &&
               ItemStack.matches(shell, that.shell);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ItemStack.hashItemAndComponents(shell), delay, reloadDelay, lastShellPending);
    }

    public static boolean getHasBulletInBarrelFromStack(ItemStack gunStack) {
        CustomData customData = gunStack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) {
            LOGGER.warn("[getHasBulletInBarrel]: the gun[{}] does not have components custom_data", gunStack);
            return false;
        }

        CompoundTag tag = customData.copyTag();

        if (tag.contains("HasBulletInBarrel", Tag.TAG_INT)) {
            boolean hasBulletInBarrel = tag.getBoolean("HasBulletInBarrel");
            return hasBulletInBarrel;
        }
        return false;
    }
}