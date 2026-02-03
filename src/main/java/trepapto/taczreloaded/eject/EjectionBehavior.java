package trepapto.taczreloaded.eject;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import trepapto.taczreloaded.event.DelayedShellManager;
import trepapto.taczreloaded.data.ModDataComponents;

public abstract class EjectionBehavior {

    protected final ItemStack shell;
    protected final float delay;

    protected EjectionBehavior(ItemStack shell, float delay) {
        this.shell = shell;
        this.delay = delay;
    }

    public ItemStack getShell() {
        return shell;
    }

    public float getDelay() {
        return delay;
    }

    public int getDelayTicks() {
        return Math.round(delay * 20f);
    }

    // ========== 抽象方法 ==========

    public abstract EjectMode getMode();

    public abstract MapCodec<? extends EjectionBehavior> codec();

    // ========== 工具方法（带回调） ==========

    protected void ejectShellWithCallback(LivingEntity entity, ItemStack gunStack, int currentAmmo, int delayTicks) {
        if (shell.isEmpty()) return;
        DelayedShellManager.scheduleShell(entity, shell.copy(), delayTicks, this, gunStack, currentAmmo);
    }

    protected void ejectShellsWithCallback(LivingEntity entity, ItemStack gunStack, int currentAmmo, int count, int delayTicks) {
        if (shell.isEmpty() || count <= 0) return;
        ItemStack shells = shell.copyWithCount(count);
        DelayedShellManager.scheduleShell(entity, shells, delayTicks, this, gunStack, currentAmmo);
    }

    // ========== Codec ==========

    public static final Codec<EjectionBehavior> CODEC = EjectMode.CODEC.dispatch(
            "mode",
            EjectionBehavior::getMode,
            EjectMode::codec
    );

    // EjectionBehavior.java 添加

    /**
     * 将当前 behavior 状态保存回 ItemStack 的 DataComponent
     */
    protected void saveTo(ItemStack gunStack) {
        gunStack.set(ModDataComponents.EJECTION_BEHAVIOR.get(), this);
    }
}