package dev.stormy.client.main;

import net.minecraft.client.Minecraft;
import net.weavemc.loader.api.event.EventBus;
import net.weavemc.loader.api.event.SubscribeEvent;
import net.weavemc.loader.api.event.TickEvent;
import dev.stormy.client.clickgui.ClickGui;
import dev.stormy.client.config.ClientConfig;
import dev.stormy.client.config.ConfigManager;
import dev.stormy.client.module.Module;
import dev.stormy.client.module.ModuleManager;
import dev.stormy.client.utils.MouseManager;
import dev.stormy.client.utils.Utils;

public class Stormy {

   public static boolean debugger = false;
   public static ConfigManager configManager;
   public static ClientConfig clientConfig;
   public static final ModuleManager moduleManager = new ModuleManager();
   public static ClickGui clickGui;

   public static void init() {
      EventBus.subscribe(new Stormy());
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