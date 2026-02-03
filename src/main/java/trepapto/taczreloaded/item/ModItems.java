package trepapto.taczreloaded.item;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static trepapto.taczreloaded.TaCZReloaded.MODID;

public class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(MODID);

    public static final DeferredItem<Item> flint_powder = ITEMS.registerItem(
            "flint_powder",
            Item::new,
            new Item.Properties()
    );

    public static final DeferredItem<Item> flint_dust = ITEMS.registerItem(
            "flint_dust",
            Item::new,
            new Item.Properties()
    );

    public static final DeferredItem<Item> SHELL = ITEMS.register(
            "shell",
            () -> new ShellItem(new Item.Properties())
    );
}