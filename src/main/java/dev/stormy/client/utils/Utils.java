package dev.stormy.client.utils;

import dev.stormy.client.main.Stormy;
import dev.stormy.client.module.Module;
import dev.stormy.client.module.setting.impl.DoubleSliderSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.weavemc.loader.api.event.EventBus;
import net.weavemc.loader.api.event.MouseEvent;
import org.lwjgl.input.Mouse;
import me.tryfle.stormy.hooks.CPSHook;
import org.lwjgl.opengl.GL11;
import dev.stormy.client.module.modules.combat.AutoClicker;

import java.awt.*;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.*;

@SuppressWarnings("unused")
public class Utils {
   private static final Random rand = new Random();
   public static final Minecraft mc = Minecraft.getMinecraft();

   public static class Player {
      public static void sendMessageToSelf(String txt) {
         if (isPlayerInGame()) {
            String m = Client.reformat("&7[&dR&7]&r " + txt);
            mc.thePlayer.addChatMessage(new ChatComponentText(m));
         }
      }

      public static boolean isPlayerInGame() {
         return mc.thePlayer != null && mc.theWorld != null;
      }

      public static boolean isPlayerMoving() {
         return mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F;
      }

      public static double fovFromEntity(Entity en) {
         return ((double) (mc.thePlayer.rotationYaw - fovToEntity(en)) % 360.0D + 540.0D) % 360.0D - 180.0D;
      }

      public static float fovToEntity(Entity ent) {
         double x = ent.posX - mc.thePlayer.posX;
         double z = ent.posZ - mc.thePlayer.posZ;
         double yaw = Math.atan2(x, z) * 57.2957795D;
         return (float) (yaw * -1.0D);
      }

      public static boolean lookingAtPlayer(EntityPlayer viewer, EntityPlayer targetPlayer, double maxDistance) {
         double deltaX = targetPlayer.posX - viewer.posX;
         double deltaY = targetPlayer.posY - viewer.posY + viewer.getEyeHeight();
         double deltaZ = targetPlayer.posZ - viewer.posZ;
         double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
         return distance < maxDistance;
      }
      public static boolean playerOverAir() {
         double x = mc.thePlayer.posX;
         double y = mc.thePlayer.posY - 1.0D;
         double z = mc.thePlayer.posZ;
         BlockPos p = new BlockPos(MathHelper.floor_double(x), MathHelper.floor_double(y), MathHelper.floor_double(z));
         return mc.theWorld.isAirBlock(p);
      }

      public static boolean isPlayerHoldingWeapon() {
         if (mc.thePlayer.getCurrentEquippedItem() == null) {
            return false;
         } else {
            Item item = mc.thePlayer.getCurrentEquippedItem().getItem();
            return item instanceof ItemSword || item instanceof ItemAxe;
         }
      }
   }

   public static class HookUtils {
      public static void setMouseButtonState(int mouseButton, boolean held) {
         MouseEvent m = new MouseEvent();
         ReflectionUtils.setPrivateValue(MouseEvent.class, m, mouseButton, "button");
         ReflectionUtils.setPrivateValue(MouseEvent.class, m, held, "buttonState");
         EventBus.callEvent(m);

         ByteBuffer buttons = (ByteBuffer) ReflectionUtils.getPrivateValue(Mouse.class, null, "buttons");
         if (buttons == null) {
            System.out.println("buttons is null, something is wrong");
            return;
         }
         buttons.put(mouseButton, (byte) (held ? 1 : 0));
         ReflectionUtils.setPrivateValue(Mouse.class, null, buttons, "buttons");
         if (held) {
            if (mouseButton == 0) {
               CPSHook.leftClick();
            } else {
               CPSHook.rightClick();
            }
         }
      }
   }

   public static class Client {
      public static void setMouseButtonState(int mouseButton, boolean held) {
         MouseEvent m = new MouseEvent();
         ReflectionUtils.setPrivateValue(MouseEvent.class, m, mouseButton, "button");
         ReflectionUtils.setPrivateValue(MouseEvent.class, m, held, "buttonState");
         EventBus.callEvent(m);

         ByteBuffer buttons = (ByteBuffer) ReflectionUtils.getPrivateValue(Mouse.class, null, "buttons");
         if (buttons == null) {
            System.out.println("buttons is null, something is wrong");
            return;
         }
         buttons.put(mouseButton, (byte) (held ? 1 : 0));
         ReflectionUtils.setPrivateValue(Mouse.class, null, buttons, "buttons");
      }

      public static double ranModuleVal(DoubleSliderSetting a, Random r) {
         return a.getInputMin() == a.getInputMax() ? a.getInputMin() : a.getInputMin() + r.nextDouble() * (a.getInputMax() - a.getInputMin());
      }

      public static boolean autoClickerClicking() {
         Module autoClicker = Stormy.moduleManager.getModuleByClazz(AutoClicker.class);
         if (autoClicker != null && autoClicker.isEnabled()) {
            return autoClicker.isEnabled() && Mouse.isButtonDown(0);
         }
         return false;
      }

      public static int rainbowDraw(long speed, long... delay) {
         long time = System.currentTimeMillis() + (delay.length > 0 ? delay[0] : 0L);
         return Color.getHSBColor((float) (time % (15000L / speed)) / (15000.0F / (float) speed), 1.0F, 1.0F).getRGB();
      }

      public static boolean currentScreenMinecraft() {
         return mc.currentScreen != null;
      }

      public static String reformat(String txt) {
         return txt.replace("&", "\u00A7");
      }
   }

   public static class Java {
      public static Random rand() {
         return rand;
      }

      public static double round(double n, int d) {
         if (d == 0) {
            return (double) Math.round(n);
         } else {
            double p = Math.pow(10.0D, d);
            return (double) Math.round(n * p) / p;
         }
      }

      public static ArrayList<String> toArrayList(String[] fakeList) {
         return new ArrayList<>(Arrays.asList(fakeList));
      }

      public static List<String> StringListToList(String[] whytho) {
         List<String> howTohackNasaWorking2021NoScamDotCom = new ArrayList<>();
         Collections.addAll(howTohackNasaWorking2021NoScamDotCom, whytho);
         return howTohackNasaWorking2021NoScamDotCom;
      }

      public static int randomInt(double inputMin, double v) {
         return (int) (Math.random() * (v - inputMin) + inputMin);
      }
   }
   public static class Distance {
      /**
       * Credit: @AriaJackie/Fractal
       * Calculates the distance to the entity.
       * @param entity    the target entity.
       * @return          the distance to the entity.
       */
      public static double distanceToEntity(final EntityPlayer entity) {
         Minecraft mcInstance = Minecraft.getMinecraft();

         float offsetX = (float) (entity.posX - mcInstance.thePlayer.posX);
         float offsetZ = (float) (entity.posZ - mcInstance.thePlayer.posZ);

         return MathHelper.sqrt_double(offsetX * offsetX + offsetZ * offsetZ);
      }

      /**
       * Credit: @AriaJackie/Fractal
       * Calculates the distance to the specified positions.
       * @param posX      the target posX.
       * @param posZ      the target posZ.
       * @return          the distance to the positions.
       */
      public static double distanceToPoses(final double posX, final double posZ) {
         Minecraft mcInstance = Minecraft.getMinecraft();

         float offsetX = (float) (posX - mcInstance.thePlayer.posX);
         float offsetZ = (float) (posZ - mcInstance.thePlayer.posZ);

         return MathHelper.sqrt_double(offsetX * offsetX + offsetZ * offsetZ);
      }
   }

   public static class HUD {
      private static final Minecraft mc = Minecraft.getMinecraft();
      public static boolean ring_c = false;

      public static void re(BlockPos bp, int color, boolean shade) {
         if (bp != null) {
            double x = (double) bp.getX() - mc.getRenderManager().viewerPosX;
            double y = (double) bp.getY() - mc.getRenderManager().viewerPosY;
            double z = (double) bp.getZ() - mc.getRenderManager().viewerPosZ;
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(3042);
            GL11.glLineWidth(2.0F);
            GL11.glDisable(3553);
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            float a = (float) (color >> 24 & 255) / 255.0F;
            float r = (float) (color >> 16 & 255) / 255.0F;
            float g = (float) (color >> 8 & 255) / 255.0F;
            float b = (float) (color & 255) / 255.0F;
            GL11.glColor4d(r, g, b, a);
            RenderGlobal.drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D));
            if (shade) {
               dbb(new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D), r, g, b);
            }

            GL11.glEnable(3553);
            GL11.glEnable(2929);
            GL11.glDepthMask(true);
            GL11.glDisable(3042);
         }
      }

      public static void drawBoxAroundEntity(Entity e, int type, double expand, double shift, int color, boolean damage) {
         if (e instanceof EntityLivingBase) {
            double x = e.lastTickPosX + (e.posX - e.lastTickPosX) * (double) mc.timer.renderPartialTicks - mc.getRenderManager().viewerPosX;
            double y = e.lastTickPosY + (e.posY - e.lastTickPosY) * (double) mc.timer.renderPartialTicks - mc.getRenderManager().viewerPosY;
            double z = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * (double) mc.timer.renderPartialTicks - mc.getRenderManager().viewerPosZ;
            float d = (float) expand / 40.0F;
            if (e instanceof EntityPlayer && damage && ((EntityPlayer) e).hurtTime != 0) {
               color = Color.RED.getRGB();
            }

            GlStateManager.pushMatrix();
            if (type == 3) {
               GL11.glTranslated(x, y - 0.2D, z);
               GL11.glRotated(-mc.getRenderManager().playerViewY, 0.0D, 1.0D, 0.0D);
               GlStateManager.disableDepth();
               GL11.glScalef(0.03F + d, 0.03F + d, 0.03F + d);
               int outline = Color.black.getRGB();
               net.minecraft.client.gui.Gui.drawRect(-20, -1, -26, 75, outline);
               net.minecraft.client.gui.Gui.drawRect(20, -1, 26, 75, outline);
               net.minecraft.client.gui.Gui.drawRect(-20, -1, 21, 5, outline);
               net.minecraft.client.gui.Gui.drawRect(-20, 70, 21, 75, outline);
               if (color != 0) {
                  net.minecraft.client.gui.Gui.drawRect(-21, 0, -25, 74, color);
                  net.minecraft.client.gui.Gui.drawRect(21, 0, 25, 74, color);
                  net.minecraft.client.gui.Gui.drawRect(-21, 0, 24, 4, color);
                  net.minecraft.client.gui.Gui.drawRect(-21, 71, 25, 74, color);
               }

               GlStateManager.enableDepth();
            } else {
               int i;
               if (type == 4) {
                  EntityLivingBase en = (EntityLivingBase) e;
                  double r = en.getHealth() / en.getMaxHealth();
                  int b = (int) (74.0D * r);
                  int hc = r < 0.3D ? Color.red.getRGB() : (r < 0.5D ? Color.orange.getRGB() : (r < 0.7D ? Color.yellow.getRGB() : Color.green.getRGB()));
                  GL11.glTranslated(x, y - 0.2D, z);
                  GL11.glRotated(-mc.getRenderManager().playerViewY, 0.0D, 1.0D, 0.0D);
                  GlStateManager.disableDepth();
                  GL11.glScalef(0.03F + d, 0.03F + d, 0.03F + d);
                  i = (int) (21.0D + shift * 2.0D);
                  net.minecraft.client.gui.Gui.drawRect(i, -1, i + 5, 75, Color.black.getRGB());
                  net.minecraft.client.gui.Gui.drawRect(i + 1, b, i + 4, 74, Color.darkGray.getRGB());
                  net.minecraft.client.gui.Gui.drawRect(i + 1, 0, i + 4, b, hc);
                  GlStateManager.enableDepth();
               } else if (type == 6) {
                  d3p(x, y, z, 0.699999988079071D, 45, 1.5F, color, color == 0);
               } else {
                  float a = (float) (color >> 24 & 255) / 255.0F;
                  float r = (float) (color >> 16 & 255) / 255.0F;
                  float g = (float) (color >> 8 & 255) / 255.0F;
                  float b = (float) (color & 255) / 255.0F;
                  if (type == 5) {
                     GL11.glTranslated(x, y - 0.2D, z);
                     GL11.glRotated(-mc.getRenderManager().playerViewY, 0.0D, 1.0D, 0.0D);
                     GlStateManager.disableDepth();
                     GL11.glScalef(0.03F + d, 0.03F, 0.03F + d);
                     d2p(0.0D, 95.0D, 10, 3, Color.black.getRGB());

                     for (i = 0; i < 6; ++i) {
                        d2p(0.0D, 95 + (10 - i), 3, 4, Color.black.getRGB());
                     }

                     for (i = 0; i < 7; ++i) {
                        d2p(0.0D, 95 + (10 - i), 2, 4, color);
                     }

                     d2p(0.0D, 95.0D, 8, 3, color);
                     GlStateManager.enableDepth();
                  } else {
                     AxisAlignedBB bbox = e.getEntityBoundingBox().expand(0.1D + expand, 0.1D + expand, 0.1D + expand);
                     AxisAlignedBB axis = new AxisAlignedBB(bbox.minX - e.posX + x, bbox.minY - e.posY + y, bbox.minZ - e.posZ + z, bbox.maxX - e.posX + x, bbox.maxY - e.posY + y, bbox.maxZ - e.posZ + z);
                     GL11.glBlendFunc(770, 771);
                     GL11.glEnable(3042);
                     GL11.glDisable(3553);
                     GL11.glDisable(2929);
                     GL11.glDepthMask(false);
                     GL11.glLineWidth(2.0F);
                     GL11.glColor4f(r, g, b, a);
                     if (type == 1) {
                        RenderGlobal.drawSelectionBoundingBox(axis);
                     } else if (type == 2) {
                        dbb(axis, r, g, b);
                     }

                     GL11.glEnable(3553);
                     GL11.glEnable(2929);
                     GL11.glDepthMask(true);
                     GL11.glDisable(3042);
                  }
               }
            }

            GlStateManager.popMatrix();
         }
      }

      public static void dbb(AxisAlignedBB abb, float r, float g, float b) {
         float a = 0.25F;
         Tessellator ts = Tessellator.getInstance();
         WorldRenderer vb = ts.getWorldRenderer();
         vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
         vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
         vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
         vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
         vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
         vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
         vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
         vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
         vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
         ts.draw();
         vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
         vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
         vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
         vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
         vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
         vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
         vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
         vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
         vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
         ts.draw();
         vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
         vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
         vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
         vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
         vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
         vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
         vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
         vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
         vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
         ts.draw();
         vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
         vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
         vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
         vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
         vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
         vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
         vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
         vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
         vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
         ts.draw();
         vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
         vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
         vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
         vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
         vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
         vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
         vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
         vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
         vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
         ts.draw();
         vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
         vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
         vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
         vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
         vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
         vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
         vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
         vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
         vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
         ts.draw();
      }

      public static PositionMode getPostitionMode(int marginX, int marginY, double height, double width) {
         int halfHeight = (int) (height / 4);
         int halfWidth = (int) width;
         PositionMode positionMode = null;

         if (marginY < halfHeight) {
            if (marginX < halfWidth) {
               positionMode = PositionMode.UPLEFT;
            }
            if (marginX > halfWidth) {
               positionMode = PositionMode.UPRIGHT;
            }
         }

         if (marginY > halfHeight) {
            if (marginX < halfWidth) {
               positionMode = PositionMode.DOWNLEFT;
            }
            if (marginX > halfWidth) {
               positionMode = PositionMode.DOWNRIGHT;
            }
         }

         return positionMode;
      }

      public static void d2p(double x, double y, int radius, int sides, int color) {
         float a = (float) (color >> 24 & 255) / 255.0F;
         float r = (float) (color >> 16 & 255) / 255.0F;
         float g = (float) (color >> 8 & 255) / 255.0F;
         float b = (float) (color & 255) / 255.0F;
         Tessellator tessellator = Tessellator.getInstance();
         WorldRenderer worldrenderer = tessellator.getWorldRenderer();
         GlStateManager.enableBlend();
         GlStateManager.disableTexture2D();
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
         GlStateManager.color(r, g, b, a);
         worldrenderer.begin(6, DefaultVertexFormats.POSITION);

         for (int i = 0; i < sides; ++i) {
            double angle = 6.283185307179586D * (double) i / (double) sides + Math.toRadians(180.0D);
            worldrenderer.pos(x + Math.sin(angle) * (double) radius, y + Math.cos(angle) * (double) radius, 0.0D).endVertex();
         }

         tessellator.draw();
         GlStateManager.enableTexture2D();
         GlStateManager.disableBlend();
      }

      public static void d3p(double x, double y, double z, double radius, int sides, float lineWidth, int color, boolean chroma) {
         float a = (float) (color >> 24 & 255) / 255.0F;
         float r = (float) (color >> 16 & 255) / 255.0F;
         float g = (float) (color >> 8 & 255) / 255.0F;
         float b = (float) (color & 255) / 255.0F;
         mc.entityRenderer.disableLightmap();
         GL11.glDisable(3553);
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 771);
         GL11.glDisable(2929);
         GL11.glEnable(2848);
         GL11.glDepthMask(false);
         GL11.glLineWidth(lineWidth);
         if (!chroma) {
            GL11.glColor4f(r, g, b, a);
         }

         GL11.glBegin(1);
         long d = 0L;
         long ed = 15000L / (long) sides;
         long hed = ed / 2L;

         for (int i = 0; i < sides * 2; ++i) {
            if (chroma) {
               if (i % 2 != 0) {
                  if (i == 47) {
                     d = hed;
                  }

                  d += ed;
               }

               int c = Client.rainbowDraw(2L, d);
               float r2 = (float) (c >> 16 & 255) / 255.0F;
               float g2 = (float) (c >> 8 & 255) / 255.0F;
               float b2 = (float) (c & 255) / 255.0F;
               GL11.glColor3f(r2, g2, b2);
            }

            double angle = 6.283185307179586D * (double) i / (double) sides + Math.toRadians(180.0D);
            GL11.glVertex3d(x + Math.cos(angle) * radius, y, z + Math.sin(angle) * radius);
         }

         GL11.glEnd();
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glDepthMask(true);
         GL11.glDisable(2848);
         GL11.glEnable(2929);
         GL11.glDisable(3042);
         GL11.glEnable(3553);
         mc.entityRenderer.enableLightmap();
      }

      public enum PositionMode {
         UPLEFT,
         UPRIGHT,
         DOWNLEFT,
         DOWNRIGHT
      }
   }
}
