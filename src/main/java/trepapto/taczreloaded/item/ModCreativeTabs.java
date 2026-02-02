package trepapto.taczreloaded.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import trepapto.taczreloaded.data.ModDataComponents;
import trepapto.taczreloaded.data.shell.ShellManager;

import static trepapto.taczreloaded.TaCZReloaded.MODID;

public class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TACZRELOADED_TAB =
            CREATIVE_TABS.register("tacz_reloaded_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + MODID + ".tacz_reloaded_tab"))
                    .icon(() -> new ItemStack(ModItems.flint_powder.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.flint_powder.get());
                        output.accept(ModItems.flint_dust.get());
                        for (ResourceLocation shellId : ShellManager.INSTANCE.getAllIds()){// 从已加载的数据动态生成
                            ItemStack stack = new ItemStack(ModItems.SHELL.get());
                            stack.set(ModDataComponents.SHELL_ID.get(), shellId.toString());
                            output.accept(stack);
                        }
                    })
                    .build()
            );
}