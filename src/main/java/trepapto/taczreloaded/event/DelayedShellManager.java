package trepapto.taczreloaded.event;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import trepapto.taczreloaded.eject.EjectionBehavior;
import trepapto.taczreloaded.eject.trigger.IOnEjectFinish;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import static trepapto.taczreloaded.TaCZReloaded.LOGGER;

public class DelayedShellManager {

    private record DelayedTask(
        long executeAt,
        UUID entityUUID,
        ItemStack shellStack,
        EjectionBehavior behavior,
        ItemStack gunStackSnapshot,
        int currentAmmo
    ) {}

    private static final CopyOnWriteArrayList<DelayedTask> PENDING_TASKS = new CopyOnWriteArrayList<>();

    public static void scheduleShell(LivingEntity entity, ItemStack shellStack, int delayTicks,
                                     EjectionBehavior behavior, ItemStack gunStackSnapshot, int currentAmmo) {
        if (delayTicks <= 0) {
            giveShell(entity, shellStack.copy());
            // 立即执行回调
            if (behavior instanceof IOnEjectFinish callback) {
                callback.onEjectFinish(entity, gunStackSnapshot, shellStack, currentAmmo);
            }
            return;
        }
        long executeAt = entity.level().getGameTime() + delayTicks;
        PENDING_TASKS.add(new DelayedTask(
            executeAt,
            entity.getUUID(),
            shellStack.copy(),
            behavior,
            gunStackSnapshot == null ? ItemStack.EMPTY : gunStackSnapshot.copy(),
            currentAmmo
        ));
    }

    // 兼容重载
    public static void scheduleShell(LivingEntity entity, ItemStack shellStack, int delayTicks) {
        scheduleShell(entity, shellStack, delayTicks, null, ItemStack.EMPTY, 0);
    }

    public static void cancelPendingShells(UUID entityUUID) {
        int before = PENDING_TASKS.size();
        PENDING_TASKS.removeIf(task -> task.entityUUID().equals(entityUUID));
        int removed = before - PENDING_TASKS.size();
        if (removed > 0) {
            LOGGER.debug("Cancelled {} pending shell tasks for {}", removed, entityUUID);
        }
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        if (PENDING_TASKS.isEmpty()) return;

        MinecraftServer server = event.getServer();
        long currentTime = server.overworld().getGameTime();

        List<DelayedTask> toRemove = new ArrayList<>();

        for (DelayedTask task : PENDING_TASKS) {
            if (currentTime >= task.executeAt()) {
                LivingEntity entity = findEntityAcrossLevels(server, task.entityUUID());

                if (entity != null && entity.isAlive()) {
                    giveShell(entity, task.shellStack());

                    // 执行回调
                    if (task.behavior() instanceof IOnEjectFinish callback) {
                        callback.onEjectFinish(entity, task.gunStackSnapshot(), task.shellStack(), task.currentAmmo());
                    }
                }

                toRemove.add(task);
            }
        }

        PENDING_TASKS.removeAll(toRemove);
    }

    private static LivingEntity findEntityAcrossLevels(MinecraftServer server, UUID uuid) {
        for (ServerLevel level : server.getAllLevels()) {
            if (level.getEntity(uuid) instanceof LivingEntity living) {
                return living;
            }
        }
        return null;
    }

    public static void giveShell(LivingEntity entity, ItemStack shellStack) {
        ItemStack remainder = insertItem(entity, shellStack);
        if (!remainder.isEmpty()) {
            spawnItemEntity(entity, remainder);
        }
    }

    private static ItemStack insertItem(LivingEntity entity, ItemStack stack) {
        if (entity instanceof Player player) {
            if (player.getInventory().add(stack)) {
                return ItemStack.EMPTY;
            }
            return stack;
        }

        ItemStack offhand = entity.getOffhandItem();
        if (offhand.isEmpty()) {
            entity.setItemSlot(EquipmentSlot.OFFHAND, stack);
            return ItemStack.EMPTY;
        }

        if (ItemStack.isSameItemSameComponents(offhand, stack)) {
            int space = offhand.getMaxStackSize() - offhand.getCount();
            int toAdd = Math.min(space, stack.getCount());
            if (toAdd > 0) {
                offhand.grow(toAdd);
                stack.shrink(toAdd);
            }
        }

        return stack;
    }

    private static void spawnItemEntity(LivingEntity entity, ItemStack stack) {
        if (stack.isEmpty()) return;
        ItemEntity item = new ItemEntity(
            entity.level(),
            entity.getX(), entity.getY() + 0.5, entity.getZ(),
            stack
        );
        item.setDefaultPickUpDelay();
        entity.level().addFreshEntity(item);
    }
}