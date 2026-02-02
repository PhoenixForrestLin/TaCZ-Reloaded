package trepapto.taczreloaded.data.shell;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

public record ShellData(
        ResourceLocation id,
        String nameKey,
        int stackSize
) {
    public static final Codec<ShellData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("id").forGetter(ShellData::id),
                    Codec.STRING.fieldOf("name").forGetter(ShellData::nameKey),
                    Codec.INT.optionalFieldOf("stack_size", 1024).forGetter(ShellData::stackSize)
            ).apply(instance, ShellData::new)
    );
}
