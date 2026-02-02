package trepapto.taczreloaded.eject.trigger;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface IOnFireTrigger {
    void onFire(LivingEntity shooter, ItemStack gunStack, int currentAmmo);
}
