package net.craftventure.audioserver;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class GeneralListener implements Listener {
    AudioServer plugin;

    public GeneralListener(AudioServer r_plugin) {
        plugin = r_plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        try {
            updateAudioAreas(event.getTo(), event.getPlayer(), false);
        } catch (Exception e) {
            LogUtil.logException("Exception raised at playerMove", e);
        }
    }

    @EventHandler
    void PlayerTeleportEvent(PlayerTeleportEvent event) {
        try {
            updateAudioAreas(event.getTo(), event.getPlayer(), true);
        } catch (Exception e) {
            LogUtil.logException("Exception raised at playerTeleport", e);
        }
    }

    public void updateAudioAreas(Location to, Player plr, boolean removeInstant) {
        try {
            for (AudioArea area : plugin.getAudioAreas()) {
                if (plr.getWorld().equals(area.getLocMax().getWorld())) {
                    if (area.LocIsInArea(to) && area.isEnabled()) {
                        area.addPlayer(plr);
                    } else {
                        if (removeInstant)
                            area.removePlayer(plr, true, 0);
                        else
                            area.removePlayer(plr);
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.logException("Exception raised at updateAudioAreas", e);
        }
    }
}
