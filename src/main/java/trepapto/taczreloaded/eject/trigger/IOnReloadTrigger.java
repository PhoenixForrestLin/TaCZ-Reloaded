package trepapto.taczreloaded.eject.trigger;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface IOnReloadTrigger {
    void onReload(LivingEntity shooter, ItemStack gunStack, int currentAmmo);
}
