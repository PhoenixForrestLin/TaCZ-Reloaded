package trepapto.taczreloaded.eject.trigger;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface IOnGunDrawTrigger {
    void onGunDraw(LivingEntity shooter, ItemStack previousGunStack, int currentAmmo);
}
