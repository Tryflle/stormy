package dev.stormy.client.config;

import dev.stormy.client.main.Stormy;
import dev.stormy.client.utils.Utils;
import dev.stormy.client.clickgui.components.CategoryComponent;
import dev.stormy.client.module.modules.client.ArrayListModule;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientConfig {
   private final File configFile;
   private final File weaveDir = new File(System.getProperty("user.home"), ".weave");
   private final File configDir = new File(weaveDir, "stormy");;
   private final String fileName = "client.scfg";
   private final String clickGuiPosPrefix = "clickgui-pos~ ";
   private final String loadedConfigPrefix = "loaded-cfg~ ";

   public ClientConfig(){
      if(!configDir.exists()){
         configDir.mkdir();
      }

      configFile = new File(configDir, fileName);
      if(!configFile.exists()){
         try {
            configFile.createNewFile();
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
   }

   public void saveConfig() {
      List<String> config = new ArrayList<>();
      config.add(clickGuiPosPrefix + getClickGuiPos());
      config.add(loadedConfigPrefix + Stormy.configManager.getConfig().getName());
      config.add(ArrayListModule.HUDX_prefix + ArrayListModule.getHudX());
      config.add(ArrayListModule.HUDY_prefix + ArrayListModule.getHudY());

      PrintWriter writer;
      try {
         writer = new PrintWriter(this.configFile);
         for (String line : config) {
            writer.println(line);
         }
         writer.close();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public void applyConfig(){
      List<String> config = this.parseConfigFile();

      for(String line : config){
         if(line.startsWith(clickGuiPosPrefix)){
            loadClickGuiCoords(line.replace(clickGuiPosPrefix, ""));
         } else if(line.startsWith(loadedConfigPrefix)){
            Stormy.configManager.loadConfigByName(line.replace(loadedConfigPrefix, ""));
         } else if (line.startsWith(ArrayListModule.HUDX_prefix)) {
            try {
               ArrayListModule.setHudX(Integer.parseInt(line.replace(ArrayListModule.HUDX_prefix, "")));
            } catch (Exception e) {e.printStackTrace();}
         } else if (line.startsWith(ArrayListModule.HUDY_prefix)) {
            try {
               ArrayListModule.setHudY(Integer.parseInt(line.replace(ArrayListModule.HUDY_prefix, "")));
            } catch (Exception e) {e.printStackTrace();}
         }
      }
   }

   private List<String> parseConfigFile() {
      List<String> configFileContents = new java.util.ArrayList<>();
      Scanner reader = null;
      try {
         reader = new Scanner(this.configFile);
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      }
      while (reader.hasNextLine())
         configFileContents.add(reader.nextLine());

      return configFileContents;
   }

   private void loadClickGuiCoords(String decryptedString) {
      for (String what : decryptedString.split("/")){
         for (CategoryComponent cat : Stormy.clickGui.getCategoryList()) {
            if(what.startsWith(cat.categoryName.name())){
               List<String> cfg = Utils.Java.StringListToList(what.split("~"));
               cat.setX(Integer.parseInt(cfg.get(1)));
               cat.setY(Integer.parseInt(cfg.get(2)));
               cat.setOpened(Boolean.parseBoolean(cfg.get(3)));
            }
         }
      }
   }

   public String getClickGuiPos() {
      StringBuilder posConfig = new StringBuilder();
      for (CategoryComponent cat : Stormy.clickGui.getCategoryList()) {
         posConfig.append(cat.categoryName.name());
         posConfig.append("~");
         posConfig.append(cat.getX());
         posConfig.append("~");
         posConfig.append(cat.getY());
         posConfig.append("~");
         posConfig.append(cat.isOpened());
         posConfig.append("/");
      }
      return posConfig.substring(0, posConfig.toString().length() - 2);

   }
}
