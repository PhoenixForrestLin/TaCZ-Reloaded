package trepapto.taczreloaded.data.shell;


import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static trepapto.taczreloaded.TaCZReloaded.LOGGER;

public class ShellManager extends SimplePreparableReloadListener<Map<ResourceLocation, ShellData>> {

    public static final ShellManager INSTANCE = new ShellManager();

    private final Map<ResourceLocation, ShellData> shells = new HashMap<>();

    private ShellManager(){}

    @Override
    protected @NotNull Map<ResourceLocation, ShellData> prepare(ResourceManager resourceManager, @NotNull ProfilerFiller profiler) {
        Map<ResourceLocation, ShellData> map = new HashMap<>();

        var resources = resourceManager.listResources("shells",
                path -> path.getPath().endsWith(".json"));

        for (var entry : resources.entrySet()) {
            ResourceLocation fileLocation = entry.getKey();
            try (Reader reader = entry.getValue().openAsReader()) {
                JsonObject json = GsonHelper.parse(reader);
                ShellData data = ShellData.CODEC.parse(JsonOps.INSTANCE, json)
                        .getOrThrow();
                map.put(data.id(), data);
                LOGGER.info("Loaded shell: {}", data.id());
            } catch (Exception e) {
                LOGGER.error("Failed to load shell file: {}", fileLocation, e);
            }
        }

        return map;
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, ShellData> data, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profiler) {
        shells.clear();
        shells.putAll(data);
        LOGGER.info("Loaded {} shells", shells.size());
    }

    public ShellData get(ResourceLocation id) {
        return shells.get(id);
    }

    public Set<ResourceLocation> getAllIds() {
        return Collections.unmodifiableSet(shells.keySet());
    }
}