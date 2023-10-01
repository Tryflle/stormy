package xyz.blowsy.raven.module.setting;

import com.google.gson.JsonObject;
import xyz.blowsy.raven.clickgui.Component;
import xyz.blowsy.raven.clickgui.components.ModuleComponent;

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
