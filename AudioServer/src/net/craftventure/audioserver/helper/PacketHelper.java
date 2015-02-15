package net.craftventure.audioserver.helper;

import net.craftventure.audioserver.AudioServer;
import net.craftventure.audioserver.packets.BasePacket;
import org.bukkit.entity.Player;

public class PacketHelper {
    public static void sendToPlayer(BasePacket packet, Player player) {
        player.sendPluginMessage(AudioServer.get(), AudioServer.AUDIOSERVER_PLUGIN_CHANNEL, packet.getAsPluginMessage(player.getName()));
    }
}
