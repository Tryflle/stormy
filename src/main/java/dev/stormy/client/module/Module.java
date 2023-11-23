package dev.stormy.client.module;

import com.google.gson.JsonObject;
import dev.stormy.client.module.setting.impl.TickSetting;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.weavemc.loader.api.event.EventBus;
import org.lwjgl.input.Keyboard;
import dev.stormy.client.module.setting.Setting;

import java.util.ArrayList;

public abstract class Module {
   protected ArrayList<Setting> settings;
   private final String moduleName;
   private final ModuleCategory moduleCategory;
   protected boolean enabled = false;
   protected boolean defaultEnabled = false;
   protected int keycode;
   protected int defaultKeycode;

   protected static Minecraft mc = Minecraft.getMinecraft();
   private boolean isToggled = false;

   @Getter @Setter
   private String suffix;

   public Module(String name, ModuleCategory category, int keybind) {
      this.moduleName = name;
      this.moduleCategory = category;
      this.settings = new ArrayList<>();
      this.keycode = keybind;
      this.defaultKeycode = keybind;
   }

   public JsonObject getConfigAsJson(){
      JsonObject settings = new JsonObject();

      for(Setting setting : this.settings){
         JsonObject settingData = setting.getConfigAsJson();
         settings.add(setting.settingName, settingData);
      }

      JsonObject data = new JsonObject();
      data.addProperty("enabled", enabled);
      data.addProperty("keycode", keycode);
      data.add("settings", settings);

      return data;
   }

   public void applyConfigFromJson(JsonObject data){
      try {
         this.keycode = data.get("keycode").getAsInt();
         setToggled(data.get("enabled").getAsBoolean());
         JsonObject settingsData = data.get("settings").getAsJsonObject();
         for (Setting setting : getSettings()) {
            if (settingsData.has(setting.getName())) {
               setting.applyConfigFromJson(
                       settingsData.get(setting.getName()).getAsJsonObject()
               );
            }
         }
      } catch (NullPointerException ignored){

      }
   }

   public void keybind() {
      if (this.keycode != 0 && this.canBeEnabled()) {
         if (!this.isToggled && Keyboard.isKeyDown(this.keycode)) {
            this.toggle();
            this.isToggled = true;
         } else if (!Keyboard.isKeyDown(this.keycode)) {
            this.isToggled = false;
         }
      }
   }

   public boolean canBeEnabled() {
      return true;
   }

   public void enable() {
      this.enabled = true;

      this.onEnable();
      EventBus.subscribe(this);
   }

   public void disable() {
      this.enabled = false;
      this.onDisable();
      EventBus.unsubscribe(this);
   }

   public void setToggled(boolean enabled) {
      if(enabled){
         enable();
      } else{
         disable();
      }
   }

   public String getName() {
      return this.moduleName;
   }

   public ArrayList<Setting> getSettings() {
      return this.settings;
   }

   public void registerSetting(Setting Setting) {
      this.settings.add(Setting);
   }

   public ModuleCategory moduleCategory() {
      return this.moduleCategory;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void onEnable() {
   }

   public void onDisable() {
   }

   public void toggle() {
      if (this.enabled) {
         this.disable();
      } else {
         this.enable();
      }
   }

   public void guiUpdate() {
   }

   public void guiButtonToggled(TickSetting b) {
   }

   public int getKeycode() {
      return this.keycode;
   }

   public void setBind(int key) {
      this.keycode = key;
   }

   public void resetToDefaults() {
      this.keycode = defaultKeycode;
      this.setToggled(defaultEnabled);

      for(Setting setting : this.settings){
         setting.resetToDefaults();
      }
   }

   public enum ModuleCategory {
      Combat, Movement, Player, Render, Client
   }
}
