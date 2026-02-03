package trepapto.taczreloaded.item;

import org.jetbrains.annotations.NotNull;

import org.jetbrains.annotations.Nullable;
import trepapto.taczreloaded.TaCZReloaded;
import trepapto.taczreloaded.data.ModDataComponents;
import trepapto.taczreloaded.data.shell.ShellData;
import trepapto.taczreloaded.data.shell.ShellManager;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import static trepapto.taczreloaded.TaCZReloaded.MODID;

public class ShellItem extends Item {

    public ShellItem(Properties props) {
        super(props);
    }

    @Nullable
    private ShellData getShellData(ItemStack stack) {
        String shellIdStr = stack.get(ModDataComponents.SHELL_ID.get());
        if (shellIdStr == null) return null;

        ResourceLocation shellId = ResourceLocation.parse(shellIdStr);
        return ShellManager.INSTANCE.get(shellId);
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        ShellData data = getShellData(stack);
        if (data != null) {
            // 传入口径的翻译组件
            return Component.translatable("shell." + MODID + "." + data.id().getPath());
        }
        return super.getName(stack);
    }

    @Override
    public int getMaxStackSize(@NotNull ItemStack stack) {
        ShellData data = getShellData(stack);
        if (data != null) {
            //TaCZReloaded.LOGGER.debug("getMaxStackSize: {}", data.id().getPath());
            return data.stackSize();
        }
        //TaCZReloaded.LOGGER.debug("getMaxStackSize: {}", stack.getCount());
        return super.getMaxStackSize(stack);
    }
}