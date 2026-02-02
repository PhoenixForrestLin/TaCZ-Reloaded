package trepapto.taczreloaded.event;

import com.tacz.guns.api.event.common.GunDrawEvent;
import com.tacz.guns.api.event.common.GunFireEvent;
import com.tacz.guns.api.event.common.GunReloadEvent;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.bus.api.SubscribeEvent;
import trepapto.taczreloaded.data.mapping.GunMappingManager;
import trepapto.taczreloaded.data.ModDataComponents;
import trepapto.taczreloaded.eject.EjectionBehavior;
import trepapto.taczreloaded.eject.trigger.IOnFireTrigger;
import trepapto.taczreloaded.eject.trigger.IOnReloadTrigger;
import trepapto.taczreloaded.eject.trigger.IOnGunDrawTrigger;
import static java.sql.Types.NULL;
import static trepapto.taczreloaded.TaCZReloaded.LOGGER;

public class ShellCollectionHandler {

    @SubscribeEvent
    public static void onGunFire(GunFireEvent event) {
        if (!event.getLogicalSide().isServer()) return;

        LivingEntity entity = event.getShooter();
        if (entity == null) return;

        ItemStack gunStack = event.getGunItemStack();
        EjectionBehavior behavior = gunStack.get(ModDataComponents.EJECTION_BEHAVIOR.get());
        int ammoCount = getGunCurrentAmmoCountFromStack(gunStack);
        if (behavior != null) {
            if (behavior instanceof IOnFireTrigger trigger) {
                trigger.onFire(entity, gunStack, ammoCount);
            }
            return;
        }

        ResourceLocation gunId = getGunIdFromStack(gunStack);
        if (gunId == null) return;
        GunMappingManager.getBehavior(gunId).ifPresent(b -> {
            if (b instanceof IOnFireTrigger trigger) {
                trigger.onFire(entity, gunStack, ammoCount);
            }
        });
    }

    @SubscribeEvent
    public static void onGunReload(GunReloadEvent event) {
        if (!event.getLogicalSide().isServer()) return;

        LivingEntity entity = event.getEntity();
        if (entity == null) return;

        ItemStack gunStack = event.getGunItemStack();
        EjectionBehavior behavior = gunStack.get(ModDataComponents.EJECTION_BEHAVIOR.get());
        int ammoCount = getGunCurrentAmmoCountFromStack(gunStack);
        if (behavior != null) {
            if (behavior instanceof IOnReloadTrigger trigger) {
                trigger.onReload(entity, gunStack, ammoCount);
            }
            return;
        }

        ResourceLocation gunId = getGunIdFromStack(gunStack);
        if (gunId == null) return;
        GunMappingManager.getBehavior(gunId).ifPresent(b -> {
            if (b instanceof IOnReloadTrigger trigger) {
                trigger.onReload(entity, gunStack, ammoCount);
            }
        });
    }

    @SubscribeEvent
    public static void onGunDraw(GunDrawEvent event) {
        if (!event.getLogicalSide().isServer()) return;

        LivingEntity entity = event.getEntity();
        if (entity == null) return;

        // 先取消之前的延迟任务（打断）
        DelayedShellManager.cancelPendingShells(entity.getUUID());

        ItemStack previousGun = event.getPreviousGunItem();
        ItemStack currentGun = event.getCurrentGunItem();
        LOGGER.debug("Gun draw event: previousGun = {}, currentGun = {}", previousGun, currentGun);

        if (currentGun == null || currentGun.isEmpty()) return;

        EjectionBehavior behavior = currentGun.get(ModDataComponents.EJECTION_BEHAVIOR.get());
        
        if (behavior instanceof IOnGunDrawTrigger trigger) {
            trigger.onGunDraw(entity, currentGun, getGunCurrentAmmoCountFromStack(currentGun));
        }
    }

    public static ResourceLocation getGunIdFromStack(ItemStack gunStack) {
        CustomData customData = gunStack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) {
            LOGGER.debug("[getGunId]: the gun[{}] does not have components custom_data", gunStack);
            return null;
        }

        CompoundTag tag = customData.copyTag();

        if (tag.contains("GunId", Tag.TAG_STRING)) {
            String gunIdStr = tag.getString("GunId");
            return ResourceLocation.parse(gunIdStr);
        }
        return null;
    }

    public static int getGunCurrentAmmoCountFromStack(ItemStack gunStack) {
        CustomData customData = gunStack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) {
            LOGGER.warn("[getGunCurrentAmmoCount]: the gun[{}] does not have components custom_data", gunStack);
            return NULL;
        }

        CompoundTag tag = customData.copyTag();

        if (tag.contains("GunCurrentAmmoCount", Tag.TAG_INT)) {
            int ammoCount = tag.getInt("GunCurrentAmmoCount");
            if (ammoCount >= 0)
                return ammoCount;
            LOGGER.warn("the components[GunCurrentAmmoCount] of gun[{}] has negative value", gunStack);
        }
        return NULL;
    }
}