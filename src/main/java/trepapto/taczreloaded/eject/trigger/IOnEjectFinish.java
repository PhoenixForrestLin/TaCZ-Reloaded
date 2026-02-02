// IOnEjectFinish.java
package trepapto.taczreloaded.eject.trigger;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * 抛壳完成后的回调接口
 */
public interface IOnEjectFinish {
    /**
     * 抛壳完成时调用
     * @param shooter 射击者
     * @param gunStack 枪械物品（快照）
     * @param shellStack 抛出的弹壳
     * @param currentAmmo 当时的弹药数
     */
    void onEjectFinish(LivingEntity shooter, ItemStack gunStack, ItemStack shellStack, int currentAmmo);
}