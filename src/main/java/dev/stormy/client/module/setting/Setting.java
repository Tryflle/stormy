package dev.stormy.client.module.setting;

import com.google.gson.JsonObject;
import dev.stormy.client.clickgui.Component;
import dev.stormy.client.clickgui.components.ModuleComponent;

public abstract class Setting {
   public String settingName;

   public Setting(String name) {
      this.settingName = name;
   }

   public String getName() {
      return this.settingName;
   }

   public abstract void resetToDefaults();
   public abstract JsonObject getConfigAsJson();

   public abstract String getSettingType();

   public abstract void applyConfigFromJson(JsonObject data);

   public abstract Component createComponent(ModuleComponent moduleComponent);
}
