package trepapto.taczreloaded.client.renderer;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import trepapto.taczreloaded.data.ModDataComponents;

import java.util.Map;
import java.util.Objects;

public class ShellRenderer extends BakedModelWrapper<BakedModel> {
    private final Map<String, BakedModel> variants;

    public ShellRenderer(BakedModel original, Map<String, BakedModel> variants) {
        super(original);
        this.variants = variants;
    }

    @Override
    public @NotNull ItemOverrides getOverrides() {
        return new ItemOverrides() {
            @Override
            public BakedModel resolve(@NotNull BakedModel model, @NotNull ItemStack stack,
                                      @Nullable ClientLevel level,
                                      @Nullable LivingEntity entity, int seed) {
                if (stack.has(ModDataComponents.SHELL_ID)) {
                    @NotNull
                    String shellId = Objects.requireNonNull(stack.get(ModDataComponents.SHELL_ID));

                    // 解析为 ResourceLocation 并获取 path
                    ResourceLocation loc = ResourceLocation.parse(shellId);
                    String key = loc.getPath();  // "9mm"

                    BakedModel variant = variants.get(key);
                    if (variant != null) {
                        return variant;
                    }
                }
                return model;
            }
        };
    }
}