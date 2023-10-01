package xyz.blowsy.raven.main;

import net.minecraft.client.Minecraft;
import net.weavemc.loader.api.event.EventBus;
import net.weavemc.loader.api.event.SubscribeEvent;
import net.weavemc.loader.api.event.TickEvent;
import xyz.blowsy.raven.clickgui.ClickGui;
import xyz.blowsy.raven.config.ClientConfig;
import xyz.blowsy.raven.config.ConfigManager;
import xyz.blowsy.raven.module.Module;
import xyz.blowsy.raven.module.ModuleManager;
import xyz.blowsy.raven.utils.MouseManager;
import xyz.blowsy.raven.utils.Utils;

public class Raven {

   public static boolean debugger = false;
   public static ConfigManager configManager;
   public static ClientConfig clientConfig;
   public static final ModuleManager moduleManager = new ModuleManager();
   public static ClickGui clickGui;

   public static void init() {
      EventBus.subscribe(new Raven());
      EventBus.subscribe(new MouseManager());

      clickGui = new ClickGui();
      configManager = new ConfigManager();
      clientConfig = new ClientConfig();
      clientConfig.applyConfig();
   }

   @SubscribeEvent
   public void onTick(TickEvent.Post e) {
      if (Utils.Player.isPlayerInGame()) {
         for (Module module : moduleManager.getModules()) {
            if (Minecraft.getMinecraft().currentScreen == null) {
               module.keybind();
            } else if (Minecraft.getMinecraft().currentScreen instanceof ClickGui) {
               module.guiUpdate();
            }
         }
      }
   }
}