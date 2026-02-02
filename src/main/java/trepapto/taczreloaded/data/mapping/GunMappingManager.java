package trepapto.taczreloaded.data.mapping;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;
import trepapto.taczreloaded.eject.EjectionBehavior;

import java.io.Reader;
import java.util.*;

import static trepapto.taczreloaded.TaCZReloaded.LOGGER;

public class GunMappingManager extends SimplePreparableReloadListener<Map<ResourceLocation, EjectionBehavior>> {

    public static final GunMappingManager INSTANCE = new GunMappingManager();
    private static final Map<ResourceLocation, EjectionBehavior> GUN_MAPPING = new HashMap<>();
    private static final String DIRECTORY = "mapping";

    @Override
    protected @NotNull Map<ResourceLocation, EjectionBehavior> prepare(
            @NotNull ResourceManager resourceManager,
            @NotNull ProfilerFiller profiler) {

        Map<ResourceLocation, EjectionBehavior> result = new HashMap<>();

        var resources = resourceManager.listResources(DIRECTORY,
                path -> path.getPath().endsWith(".json"));

        for (var entry : resources.entrySet()) {
            ResourceLocation fileLocation = entry.getKey();
            ResourceLocation gunId = extractGunId(fileLocation);
            if (gunId == null) continue;

            try (Reader reader = entry.getValue().openAsReader()) {
                JsonObject json = GsonHelper.parse(reader);

                // 直接使用 EjectionBehavior 的 Codec 解析完整行为定义
                EjectionBehavior behavior = trepapto.taczreloaded.eject.EjectionBehavior.CODEC
                    .parse(JsonOps.INSTANCE, json)
                    .getOrThrow();

                result.put(gunId, behavior);
                LOGGER.debug("Loaded gun mapping: {} → {} ({})",
                    gunId, behavior.getShell(), behavior.getMode());

            } catch (Exception e) {
                LOGGER.error("Failed to load gun mapping: {}", fileLocation, e);
            }
        }
        return result;
    }

    private ResourceLocation extractGunId(ResourceLocation fileLocation) {
        String path = fileLocation.getPath();
        if (!path.startsWith(DIRECTORY + "/") || !path.endsWith(".json")) {
            return null;
        }
        String stripped = path.substring(DIRECTORY.length() + 1, path.length() - 5);
        int slashIndex = stripped.indexOf('/');
        if (slashIndex == -1) {
            return ResourceLocation.fromNamespaceAndPath(fileLocation.getNamespace(), stripped);
        }
        String namespace = stripped.substring(0, slashIndex);
        String gunPath = stripped.substring(slashIndex + 1);
        return ResourceLocation.fromNamespaceAndPath(namespace, gunPath);
    }

    @Override
    protected void apply(
            @NotNull Map<ResourceLocation, EjectionBehavior> data,
            @NotNull ResourceManager resourceManager,
            @NotNull ProfilerFiller profiler) {

        GUN_MAPPING.clear();
        GUN_MAPPING.putAll(data);
        LOGGER.info("Loaded {} gun mappings", GUN_MAPPING.size());
    }

    // ========== API ==========

    public static Optional<EjectionBehavior> getBehavior(ResourceLocation gunId) {
        return Optional.ofNullable(GUN_MAPPING.get(gunId));
    }
}