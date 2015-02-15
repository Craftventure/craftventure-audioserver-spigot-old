package net.craftventure.audioserver;

import net.craftventure.audioserver.helper.PacketHelper;
import net.craftventure.audioserver.packets.PacketAreaStart;
import net.craftventure.audioserver.packets.PacketAreaStop;
import net.craftventure.audioserver.packets.PacketAudioSync;
import net.craftventure.audioserver.packets.ProtocolConfig;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AudioArea {
    public static int VEHICLE_DEFAULT = 0;
    public static int VEHICLE_ONRIDE = 1;
    public static int VEHICLE_OFFRIDE = 2;

    private AudioServer plugin = null;
    private Location loc1;
    private Location loc2;
    private String soundname = "";
    private final String name;
    private int fadetime;
    private double volume;
    private final String type;
    private final int audioid = ProtocolConfig.getAudioID();
    private boolean startonenter;
    private int vehicletype;
    private boolean repeat;

    private final List<String> PlayersInArea = new ArrayList<String>();

    private boolean enabled = true;
    private boolean defaultState = true;

    public static String getType(int type) {
        if (type == VEHICLE_ONRIDE)
            return "onride";
        else if (type == VEHICLE_OFFRIDE)
            return "offride";
        return "default";
    }

    public AudioArea(AudioServer plugin, String name, String soundname, int fadetime, double volume, String type, Location loc1, Location loc2, boolean enabled, boolean startonenter, int vehicletype, boolean repeat) {
        this.defaultState = enabled;
        this.enabled = enabled;
        this.plugin = plugin;
        this.soundname = soundname;
        this.fadetime = fadetime;
        this.volume = volume;
        this.type = type;
        this.name = name;
        this.startonenter = startonenter;
        this.vehicletype = vehicletype;
        this.repeat = repeat;

        Location newLoc1 = loc1.clone();
        Location newLoc2 = loc2.clone();

        if (loc1.getX() > loc2.getX()) {
            newLoc1.setX(loc2.getX());
            newLoc2.setX(loc1.getX());
        }
        if (loc1.getY() > loc2.getY()) {
            newLoc1.setY(loc2.getY());
            newLoc2.setY(loc1.getY());
        }
        if (loc1.getZ() > loc2.getZ()) {
            newLoc1.setZ(loc2.getZ());
            newLoc2.setZ(loc1.getZ());
        }

        this.loc1 = newLoc1;
        this.loc2 = newLoc2;
    }

    public void setArea(Location loc1, Location loc2) {
        this.loc1 = loc1;
        this.loc2 = loc2;
    }

    public Location getLocMin() {
        return loc1;
    }

    public Location getLocMax() {
        return loc2;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setDefaultState(boolean defaultState) {
        this.defaultState = defaultState;
    }

    public boolean getDefaultState() {
        return defaultState;
    }

    public void triggerPlayer(Player plr) {
        if (this.vehicletype != AudioArea.VEHICLE_DEFAULT) {
            if (plr.isInsideVehicle() && this.vehicletype == AudioArea.VEHICLE_OFFRIDE)
                return;
            else if (plr.isInsideVehicle() && this.vehicletype != AudioArea.VEHICLE_ONRIDE)
                return;
        }

        if (PlayersInArea.contains(plr.getName())) {
            PacketHelper.sendToPlayer(new PacketAreaStart(audioid, soundname, (float) volume, fadetime, repeat), plr);
        }
    }

    public void addPlayerIfInside(Player plr) {
        for (AudioArea area : plugin.getAudioAreas()) {
            if (plr.getWorld().equals(area.getLocMax().getWorld())) {
                if (area.LocIsInArea(plr.getLocation()) && area.isEnabled()) {
                    area.addPlayer(plr);
                }
            }
        }
    }

    public void addPlayer(Player plr) {
        if (plr.isInsideVehicle() && this.vehicletype == AudioArea.VEHICLE_OFFRIDE) {
            removePlayer(plr, true);
            return;
        } else if (!plr.isInsideVehicle() && this.vehicletype == AudioArea.VEHICLE_ONRIDE) {
            removePlayer(plr, true);
            return;
        }

        if (!PlayersInArea.contains(plr.getName())) {
            PlayersInArea.add(plr.getName());
            if (startonenter)
                PacketHelper.sendToPlayer(new PacketAreaStart(audioid, soundname, (float) volume, fadetime, repeat), plr);
        }
    }

    public void sync(float time) {
        synchronized (PlayersInArea) {
            for (String playername : PlayersInArea) {
                Player plr = Bukkit.getPlayer(playername);
                if (plr != null) {
                    PacketHelper.sendToPlayer(new PacketAudioSync(audioid, time), plr);
                }
            }
        }
    }

    public void sync(float time, Player plr) {
        synchronized (PlayersInArea) {
            PacketHelper.sendToPlayer(new PacketAudioSync(audioid, time), plr);
        }
    }

    public void sync(float time, Player plr, double margin) {
        synchronized (PlayersInArea) {
            PacketHelper.sendToPlayer(new PacketAudioSync(audioid, time, margin), plr);
        }
    }

    public void removeAllPlayers(boolean sendpacket) {
        synchronized (PlayersInArea) {
            for (String playername : PlayersInArea) {
                Player plr = Bukkit.getPlayer(playername);
                if (sendpacket) {
                    PacketHelper.sendToPlayer(new PacketAreaStop(audioid, fadetime), plr);
                }
            }
            PlayersInArea.clear();
        }
    }

    public void removePlayer(Player plr) {
        removePlayer(plr, true);
    }

    public void removePlayer(Player plr, boolean sendpacket) {
        removePlayer(plr, sendpacket, fadetime);
    }

    public void removePlayer(Player plr, boolean sendpacket, int fadetime) {
        if (PlayersInArea.remove(plr.getName())) {
            if (sendpacket) {
                PacketHelper.sendToPlayer(new PacketAreaStop(audioid, fadetime), plr);
            }
        }
    }

    public void disableToPlayer(Player plr) {
        if (PlayersInArea.contains(plr.getName())) {
            PacketHelper.sendToPlayer(new PacketAreaStop(audioid, fadetime), plr);
        }
    }

    public boolean isInArea(Player plr) {
        return PlayersInArea.contains(plr.getName());
    }

    public String getAreaName() {
        return name;
    }

    public String getSoundName() {
        return soundname;
    }

    public boolean getRepeat() {
        return repeat;
    }

    public int getVehicleType() {
        return vehicletype;
    }

    public boolean LocIsInArea(Location a) {
        if (a.getWorld().getName().equalsIgnoreCase(loc1.getWorld().getName()))
            if (a.getX() > loc1.getBlockX() && a.getX() < loc2.getBlockX())
                if (a.getY() > loc1.getBlockY() && a.getY() < loc2.getBlockY())
                    if (a.getZ() > loc1.getBlockZ() && a.getZ() < loc2.getBlockZ())
                        return true;
        return false;
    }

    public void sendOptions(CommandSender sender) {
        sender.sendMessage("§6--- [" + name + "] ---");
        sender.sendMessage("§3Fade time: §e" + fadetime);
        sender.sendMessage("§9Volume: §e" + volume);
        sender.sendMessage("§3Type: §e" + type);
        sender.sendMessage("§9Soundname: §e" + soundname);
        sender.sendMessage("§3Enabled: §e" + enabled);
        sender.sendMessage("§9Start on enter: §e" + startonenter);
        sender.sendMessage("§3World: §e" + loc1.getWorld().getName());
        sender.sendMessage("§3Repeat: §e" + repeat);
        sender.sendMessage("§3Type: §e" + getType(vehicletype));
        if (PlayersInArea.size() > 0) {
            sender.sendMessage("§9Players: §e" + StringUtils.join(PlayersInArea, ", "));
        } else
            sender.sendMessage("§9There are currently no players in this area");
    }

    public String setOption(String option) {
        String[] optionvalue = option.split(":");
        if (optionvalue.length == 2) {
            if (optionvalue[0].equalsIgnoreCase("startonenter")) {
                startonenter = optionvalue[1].equalsIgnoreCase("true") || optionvalue[1].equalsIgnoreCase("1");
                return "§eThe option §6[startonenter]§e has been set to §6[" + startonenter + "]";
            } else if (optionvalue[0].equalsIgnoreCase("enabled")) {
                enabled = optionvalue[1].equalsIgnoreCase("true") || optionvalue[1].equalsIgnoreCase("1");
                return "§eThe option §6[enabled]§e has been set to §6[" + enabled + "]";
            } else if (optionvalue[0].equalsIgnoreCase("repeat")) {
                repeat = optionvalue[1].equalsIgnoreCase("true") || optionvalue[1].equalsIgnoreCase("1");
                return "§eThe option §6[repeat]§e has been set to §6[" + repeat + "]";
            } else if (optionvalue[0].equalsIgnoreCase("fadetime")) {
                fadetime = plugin.getIntFromString(optionvalue[1], 500);
                return "§eThe option §6[fadetime]§e has been set to §6[" + fadetime + "]";
            } else if (optionvalue[0].equalsIgnoreCase("vehicletype")) {
                if (optionvalue[1].equalsIgnoreCase("default")) {
                    vehicletype = plugin.getIntFromString(optionvalue[1], AudioArea.VEHICLE_DEFAULT);
                    return "§eThe option §6[vehicletype]§e has been set to §6[default]";
                } else if (optionvalue[1].equalsIgnoreCase("onride")) {
                    vehicletype = plugin.getIntFromString(optionvalue[1], AudioArea.VEHICLE_ONRIDE);
                    return "§eThe option §6[vehicletype]§e has been set to §6[onride]";
                } else if (optionvalue[1].equalsIgnoreCase("offride")) {
                    vehicletype = plugin.getIntFromString(optionvalue[1], AudioArea.VEHICLE_OFFRIDE);
                    return "§eThe option §6[vehicletype]§e has been set to §6[offride]";
                }
                vehicletype = plugin.getIntFromString(optionvalue[1], AudioArea.VEHICLE_DEFAULT);
                return "§eThe option §6[vehicletype]§e has been set to §6[" + vehicletype + "]";
            } else if (optionvalue[0].equalsIgnoreCase("volume")) {
                volume = plugin.getDoubleFromString(optionvalue[1], 1.0);
                return "§eThe option §6[volume]§e has been set to §6[" + volume + "]";
            } else if (optionvalue[0].equalsIgnoreCase("soundname")) {
                soundname = optionvalue[1];
                return "§eThe option §6[soundname]§e has been set to §6[" + soundname + "]";
            }
            return "§eThe option §6[" + optionvalue[0] + "]§e isn't recognised!";
        } else
            return "§eThe option §6[" + option + "]§e isn't using the correct syntax of option:value";
    }

    public void saveToConfig(FileConfiguration config) {
        ConfigurationSection areaSection = config.createSection("areas." + name);
        try {
            areaSection.set("world", loc1.getWorld().getName());
        } catch (Exception e) {
            areaSection.set("world", "");
        }
        areaSection.set("fadetime", fadetime);
        areaSection.set("volume", volume);
        areaSection.set("type", type);
        areaSection.set("soundname", soundname);
        areaSection.set("enabled", defaultState);
        areaSection.set("startonenter", startonenter);
        areaSection.set("repeat", repeat);
        areaSection.set("vehicletype", vehicletype);

        ConfigurationSection corner_1 = areaSection.createSection("corner_1");
        corner_1.set("x", loc1.getBlockX());
        corner_1.set("y", loc1.getBlockY());
        corner_1.set("z", loc1.getBlockZ());

        ConfigurationSection corner_2 = areaSection.createSection("corner_2");
        corner_2.set("x", loc2.getBlockX());
        corner_2.set("y", loc2.getBlockY());
        corner_2.set("z", loc2.getBlockZ());
        try {
            config.save(plugin.getAreaFile());
        } catch (IOException e) {
            e.printStackTrace();
            Bukkit.broadcast("§4Failed to save Audio Area " + name, "audio.errornotify");
        }
    }
}
