package trepapto.taczreloaded.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import trepapto.taczreloaded.data.ModDataComponents;
import trepapto.taczreloaded.item.ModItems;

import static trepapto.taczreloaded.TaCZReloaded.MODID;

@JeiPlugin
public class ModJeiPlugin implements IModPlugin {

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(MODID, "jei_plugin");
    }

    @Override
    public void registerItemSubtypes(@NotNull ISubtypeRegistration registration) {
        // 注册弹壳的子类型解释器
        registration.registerSubtypeInterpreter(
                ModItems.SHELL.get(),
                new ShellSubtypeInterpreter()
        );
    }

    // 子类型解释器：告诉 JEI 如何区分不同组件的物品
    private static class ShellSubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {

        @Override
        public @Nullable Object getSubtypeData(@NotNull ItemStack stack, @NotNull UidContext context) {
            return stack.get(ModDataComponents.SHELL_ID.get());
        }

        @Override
        @Deprecated
        public @NotNull String getLegacyStringSubtypeInfo(@NotNull ItemStack stack, @NotNull UidContext context) {
            String shellId = stack.get(ModDataComponents.SHELL_ID.get());
            return shellId != null ? shellId : "";
        }
    }
}