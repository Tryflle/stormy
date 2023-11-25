package dev.stormy.client.module.modules.player;

import dev.stormy.client.module.Module;
import dev.stormy.client.module.setting.impl.DescriptionSetting;
import dev.stormy.client.module.setting.impl.DoubleSliderSetting;
import dev.stormy.client.utils.client.ClientUtils;
import dev.stormy.client.utils.player.PlayerUtils;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.util.BlockPos;
import net.weavemc.loader.api.event.SubscribeEvent;
import net.weavemc.loader.api.event.TickEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sassan
 * 24.11.2023, 2023
 */
public class Stealer extends Module {
    public static DoubleSliderSetting delay;

    private final List<BlockPos> chests = new ArrayList<>();

    public Stealer() {
        super("Stealer", ModuleCategory.Player, 0);
        this.registerSetting(new DescriptionSetting("No logic yet"));
        this.registerSetting(delay = new DoubleSliderSetting("Delay", 0, 1000, 0, 1000, 1));
    }

    @Override
    public void onEnable() {
        chests.clear();
    }

    @SubscribeEvent
    public void onTick(TickEvent e) {
        if(ClientUtils.currentScreenMinecraft()) return;

        if (!PlayerUtils.isPlayerInGame()) return;

        if (mc.thePlayer.openContainer instanceof ContainerChest) {
            ContainerChest chest = (ContainerChest) mc.thePlayer.openContainer;

            if (chests.contains(chest.getLowerChestInventory().getName())) {
                for (int i = 0; i < chest.getLowerChestInventory().getSizeInventory(); i++) {
                    if (chest.getLowerChestInventory().getStackInSlot(i) != null) {
                        if (!isInventoryFull()) {
                            mc.playerController.windowClick(chest.windowId, i, 0, 1, mc.thePlayer);
                        }
                    }
                }
            }
        }
    }

    private boolean isInventoryFull() {
        for (int i = 9; i < 36; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getStack() == null) {
                return false;
            }
        }

        return true;
    }
}
