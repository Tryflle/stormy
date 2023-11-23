package dev.stormy.client.utils.game;

import dev.stormy.client.utils.player.PlayerUtils;
import net.weavemc.loader.api.event.MouseEvent;
import net.weavemc.loader.api.event.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class MouseManager {
   private static final List<Long> leftClicks = new ArrayList<>();
   private static final List<Long> rightClicks = new ArrayList<>();
   public static long leftClickTimer = 0L;
   public static long rightClickTimer = 0L;

   @SubscribeEvent
   public void onMouseUpdate(MouseEvent mouse) {
      if (mouse.getButtonState()) {
         if (mouse.getButton() == 0) {
            addLeftClick();
         } else if (mouse.getButton() == 1) {
            addRightClick();
         }
      }
   }

   public static void addLeftClick() {
      leftClicks.add(leftClickTimer = System.currentTimeMillis());
   }

   public static void addRightClick() {
      rightClicks.add(rightClickTimer = System.currentTimeMillis());
   }


   //prev f
   public static int getLeftClickCounter() {
      if(!PlayerUtils.isPlayerInGame()) return leftClicks.size();

      for(Long lon : leftClicks) {
         if(lon < System.currentTimeMillis() - 1000L){
            leftClicks.remove(lon);
            break;
         }
      }
      return leftClicks.size();
   }


   // prev i
   public static int getRightClickCounter() {
      if(!PlayerUtils.isPlayerInGame())return leftClicks.size();
      for(Long lon : rightClicks) {
         if(lon < System.currentTimeMillis() - 1000L){
            rightClicks.remove(lon);
            break;
         }
      }
      return rightClicks.size();
   }
}
