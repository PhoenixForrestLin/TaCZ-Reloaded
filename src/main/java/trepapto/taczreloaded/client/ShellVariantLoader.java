package trepapto.taczreloaded.client;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import com.google.gson.JsonElement;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;

import static trepapto.taczreloaded.TaCZReloaded.LOGGER;
import static trepapto.taczreloaded.TaCZReloaded.MODID;

public class ShellVariantLoader implements PreparableReloadListener {
    public static Map<String, ResourceLocation> VARIANT_CONFIG = new HashMap<>();

    public static void loadSync(ResourceManager resourceManager) {
        VARIANT_CONFIG.clear();

        try {
            Optional<Resource> resourceOpt = resourceManager.getResource(
                    ResourceLocation.fromNamespaceAndPath(MODID, "models/item/shell_variants.json")
            );

            if (resourceOpt.isPresent()) {
                try (BufferedReader reader = resourceOpt.get().openAsReader()) {
                    JsonObject json = GsonHelper.parse(reader);
                    JsonObject variants = json.getAsJsonObject("variants");

                    for (Map.Entry<String, JsonElement> entry : variants.entrySet()) {
                        VARIANT_CONFIG.put(
                                entry.getKey(),
                                ResourceLocation.parse(entry.getValue().getAsString())
                        );
                    }
                    LOGGER.info("Loaded {} shell variants", VARIANT_CONFIG.size());
                }
            } else {
                LOGGER.warn("shell_variants.json not found");
            }
        } catch (IOException e) {
            LOGGER.error("Failed to load shell variants", e);
        }
    }

    @Override
    public @NotNull CompletableFuture<Void> reload(
            PreparationBarrier barrier,
            @NotNull ResourceManager resourceManager,
            @NotNull ProfilerFiller prepareProfiler,
            @NotNull ProfilerFiller applyProfiler,
            @NotNull Executor backgroundExecutor,
            @NotNull Executor gameExecutor
    ) {
        return CompletableFuture.runAsync(
                () -> loadSync(resourceManager),
                backgroundExecutor
        ).thenCompose(barrier::wait);
    }

    @Override
    public @NotNull String getName() {
        return "Shell Variant Loader";
    }
}