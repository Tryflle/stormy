package dev.stormy.client.module.modules.player;

import dev.stormy.client.module.Module;
import dev.stormy.client.module.setting.impl.DescriptionSetting;
import dev.stormy.client.module.setting.impl.TickSetting;
import dev.stormy.client.utils.player.PlayerUtils;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.weavemc.loader.api.event.SubscribeEvent;
import net.weavemc.loader.api.event.TickEvent;
import net.weavemc.loader.api.event.WorldEvent;

@SuppressWarnings("unused")
public class AutoDodge extends Module {
    public static TickSetting notify;
    public boolean shouldNotify = false;

    public AutoDodge() {
        super("AutoDodge", ModuleCategory.Player, 0);
        this.registerSetting(new DescriptionSetting("Currently just lists teams."));
        this.registerSetting(notify = new TickSetting("Notify if player is in your game", true));
    }

    @SubscribeEvent
    public void checkTeamsExist(TickEvent e) {
        if (shouldNotify && PlayerUtils.isPlayerInGame()) {
            for (ScorePlayerTeam team : mc.theWorld.getScoreboard().getTeams()) {
                String teamName = team.getRegisteredName();
                mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_PURPLE + "Raven: " + EnumChatFormatting.AQUA + "Team: " + teamName));
                shouldNotify = false;
            }
        }
    }

    public void di(NetworkPlayerInfo o1, NetworkPlayerInfo o2) {
        long team1 = o1.getPlayerTeam().func_98299_i();
        long team2 = o2.getPlayerTeam().func_98299_i();
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent e) {
        shouldNotify = true;
    }
}