package trepapto.taczreloaded.event;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;

public class ModEvent {
    public static void register(IEventBus modEventBus) {
        NeoForge.EVENT_BUS.register(ShellCollectionHandler.class);
        NeoForge.EVENT_BUS.register(DelayedShellManager.class);
    }
}
