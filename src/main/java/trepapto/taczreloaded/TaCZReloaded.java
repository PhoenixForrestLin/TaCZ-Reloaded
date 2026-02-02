package trepapto.taczreloaded;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import trepapto.taczreloaded.data.mapping.GunMappingManager;
import trepapto.taczreloaded.data.shell.ShellManager;
import trepapto.taczreloaded.event.ModEvent;
import trepapto.taczreloaded.item.ModItems;
import trepapto.taczreloaded.item.ModCreativeTabs;
import trepapto.taczreloaded.data.ModDataComponents;
import trepapto.taczreloaded.client.TaCZReloadedClient;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(TaCZReloaded.MODID)
public class TaCZReloaded {
    public static final String MODID = "taczreloaded";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    public TaCZReloaded(IEventBus modEventBus, ModContainer modContainer) {
        ModItems.ITEMS.register(modEventBus);
        ModCreativeTabs.CREATIVE_TABS.register(modEventBus);
        ModDataComponents.DATA_COMPONENTS.register(modEventBus);
        NeoForge.EVENT_BUS.addListener(this::onAddReloadListener);
        ModEvent.register(modEventBus);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            TaCZReloadedClient.register(modEventBus);
        }
    }
    private void onAddReloadListener(AddReloadListenerEvent event) {
        event.addListener(ShellManager.INSTANCE);
        event.addListener(GunMappingManager.INSTANCE);
        LOGGER.info("ShellManager registered!");
    }
}
