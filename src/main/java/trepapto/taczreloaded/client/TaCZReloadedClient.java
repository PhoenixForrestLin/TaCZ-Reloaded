package trepapto.taczreloaded.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.bus.api.SubscribeEvent;
import trepapto.taczreloaded.client.renderer.ShellRenderer;

import java.util.HashMap;
import java.util.Map;

import static trepapto.taczreloaded.TaCZReloaded.LOGGER;
import static trepapto.taczreloaded.TaCZReloaded.MODID;

public class TaCZReloadedClient {

    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(TaCZReloadedClient::onRegisterReloadListener);
        modEventBus.addListener(TaCZReloadedClient::onModifyBakingResult);
        modEventBus.addListener(TaCZReloadedClient::onRegisterAdditional);
    }

    @SubscribeEvent
    public static void onRegisterAdditional(ModelEvent.RegisterAdditional event) {
        ResourceManager manager = Minecraft.getInstance().getResourceManager();
        ShellVariantLoader.loadSync(manager);

        LOGGER.info("RegisterAdditional: VARIANT_CONFIG size = {}",
        ShellVariantLoader.VARIANT_CONFIG.size());

        for (ResourceLocation modelPath : ShellVariantLoader.VARIANT_CONFIG.values()) {
            event.register(ModelResourceLocation.standalone(modelPath));
            LOGGER.debug("Registered additional model: {}", modelPath);
        }
    }

    @SubscribeEvent
    public static void onRegisterReloadListener(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(new ShellVariantLoader());
    }

    @SubscribeEvent
    public static void onModifyBakingResult(ModelEvent.ModifyBakingResult event) {
        LOGGER.debug("[INFO|TaCZReloadedClient.onModifyBakingResult] start");
        Map<ModelResourceLocation, BakedModel> models = event.getModels();
        Map<String, BakedModel> variants = new HashMap<>();

        for (Map.Entry<String, ResourceLocation> entry : ShellVariantLoader.VARIANT_CONFIG.entrySet()) {
            String variantId = entry.getKey();
            ResourceLocation modelPath = entry.getValue();

            // 用 standalone 获取
            ModelResourceLocation mrl = ModelResourceLocation.standalone(modelPath);
            BakedModel variantModel = models.get(mrl);

            if (variantModel != null) {
                variants.put(variantId, variantModel);
                LOGGER.debug("[INFO|TaCZReloadedClient.onModifyBakingResult] register variant {}: {}", variantId, variantModel);
            }
        }

        // 包装主物品模型
        ModelResourceLocation shellItem = ModelResourceLocation.inventory(
                ResourceLocation.fromNamespaceAndPath(MODID, "shell")
        );
        BakedModel original = models.get(shellItem);
        if (original != null) {
            models.put(shellItem, new ShellRenderer(original, variants));
            LOGGER.debug("[INFO|TaCZReloadedClient.onModifyBakingResult] register shell item: {}", original);
        }
        else{
            LOGGER.debug("[INFO|TaCZReloadedClient.onModifyBakingResult] no shell item model");
        }
        LOGGER.debug("[INFO|TaCZReloadedClient.onModifyBakingResult] end");
    }
}
