package net.craftventure.audioserver.commands;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;
import net.craftventure.audioserver.AudioArea;
import net.craftventure.audioserver.AudioServer;
import net.craftventure.audioserver.helper.PacketHelper;
import net.craftventure.audioserver.packets.PacketGlobalPlayOnce;
import net.craftventure.audioserver.packets.ProtocolConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandAudio implements CommandExecutor {
    private final String unsufficient_permissions = "§4You don't own the right permissions to do this";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (s.equalsIgnoreCase("setaudio") || s.equalsIgnoreCase("audio")) {
            List<AudioArea> AudioAreas = AudioServer.get().getAudioAreas();
            if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("loginsync")) {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        for (AudioArea area : AudioAreas) {
                            area.removePlayer(player);
                            area.addPlayerIfInside(player);
                        }
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ach add " + player.getName() + " special audioserver");
                    }
                } else if (args[0].equalsIgnoreCase("enable")) {
                    if (!sender.hasPermission("audioserver.enable")) {
                        sender.sendMessage(unsufficient_permissions);
                    } else {
                        if (args.length >= 2) {
                            if (AudioAreas.size() > 0) {
                                for (AudioArea area : AudioAreas) {
                                    if (area.getAreaName().equalsIgnoreCase(args[1])) {
                                        area.setEnabled(true);
                                        sender.sendMessage("§2Area state enabled!");
                                        return true;
                                    }
                                }
                                sender.sendMessage("§4The area [§c" + args[1] + "§4] was not found!");
                            } else {
                                sender.sendMessage("§4There are no audio areas yet!");
                            }
                        } else {
                            sender.sendMessage("§4Usage: /audio enable <areaname>");
                        }
                    }
                } else if (args[0].equalsIgnoreCase("disable")) {
                    if (!sender.hasPermission("audioserver.disable")) {
                        sender.sendMessage(unsufficient_permissions);
                    } else {
                        if (args.length >= 2) {
                            if (AudioAreas.size() > 0) {
                                for (AudioArea area : AudioAreas) {
                                    if (area.getAreaName().equalsIgnoreCase(args[1])) {
                                        area.setEnabled(false);
                                        area.removeAllPlayers(true);
                                        sender.sendMessage("§2Area state disabled!");
                                        return true;
                                    }
                                }
                                sender.sendMessage("§4The area [" + args[1] + "] was not found!");
                            } else {
                                sender.sendMessage("§4There are no audio areas yet!");
                            }
                        } else {
                            sender.sendMessage("§4Usage: /audio disable <areaname>");
                        }
                    }
                } else if (args[0].equalsIgnoreCase("disableforplayer")) {
                    if (!sender.hasPermission("audioserver.disable")) {
                        sender.sendMessage(unsufficient_permissions);
                    } else {
                        if (args.length >= 3) {
                            Player player = Bukkit.getPlayer(args[2]);
                            if (player == null) {
                                sender.sendMessage("That player is not online");
                                return true;
                            }
                            if (AudioAreas.size() > 0) {
                                for (AudioArea area : AudioAreas) {
                                    if (area.getAreaName().equalsIgnoreCase(args[1])) {
                                        area.disableToPlayer(player);
                                        return true;
                                    }
                                }
                                sender.sendMessage("§4The area [" + args[1]
                                        + "] was not found!");
                            } else {
                                sender.sendMessage("§4There are no audio areas yet!");
                            }
                        } else {
                            sender.sendMessage("§4Usage: /audio disableforplayer <areaname> <player>");
                        }
                    }
                } else if (args[0].equalsIgnoreCase("syncareatoplayer")) {
                    if (!sender
                            .hasPermission("audioserver.syncareatoplayer")) {
                        sender.sendMessage(unsufficient_permissions);
                    } else {
                        if (args.length >= 4) {
                            Player player = Bukkit.getPlayer(args[3]);
                            if (player != null) {
                                if (AudioAreas.size() > 0) {
                                    for (AudioArea area : AudioAreas) {
                                        if (area.getAreaName().equalsIgnoreCase(args[1])) {
                                            if (args.length >= 5) {
                                                double margin = Double.parseDouble(args[4]);
                                                area.sync(
                                                        AudioServer.get().getFloatFromString(args[2], 0),
                                                        player,
                                                        margin);
                                            } else
                                                area.sync(
                                                        AudioServer.get().getFloatFromString(args[2], 0),
                                                        player);
                                            return true;
                                        }
                                    }
                                    sender.sendMessage("§4The area ["
                                            + args[1] + "] was not found!");
                                } else {
                                    sender.sendMessage("§4There are no audio areas yet!");
                                }
                            } else {
                                sender.sendMessage("§4That player wasn't found");
                            }
                        } else {
                            sender.sendMessage("§4Usage: /audio syncareatoplayer <areaname> <seconds> <player> [margin]");
                        }
                    }
                } else if (args[0].equalsIgnoreCase("syncarea")) {
                    if (!sender.hasPermission("audioserver.syncarea")) {
                        sender.sendMessage(unsufficient_permissions);
                    } else {
                        if (args.length >= 3) {
                            if (AudioAreas.size() > 0) {
                                for (AudioArea area : AudioAreas) {
                                    if (area.getAreaName()
                                            .equalsIgnoreCase(args[1])) {
                                        if (args.length >= 4) {
                                            float radius = AudioServer.get().getFloatFromString(
                                                    args[1], 1.0f);
                                            Location loc = null;
                                            if (sender instanceof BlockCommandSender) {
                                                loc = ((BlockCommandSender) sender)
                                                        .getBlock()
                                                        .getLocation();
                                            } else if (sender instanceof Player) {
                                                loc = ((Player) sender)
                                                        .getLocation();
                                            }
                                            if (loc != null) {
                                                for (Player plr : Bukkit.getOnlinePlayers()) {
                                                    if (plr.getLocation().distanceSquared(loc) <= radius * radius) {
                                                        area.sync(
                                                                AudioServer.get().getFloatFromString(args[2], 0),
                                                                plr);
                                                    }
                                                }
                                            } else
                                                sender.sendMessage("§4You don't seem to be able to perfom this command using a radius");
                                        } else
                                            area.sync(AudioServer.get().getFloatFromString(
                                                    args[2], 0));
                                        return true;
                                    }
                                }
                                sender.sendMessage("§4The area [" + args[1]
                                        + "] was not found!");
                            } else {
                                sender.sendMessage("§4There are no audio areas yet!");
                            }
                        } else {
                            sender.sendMessage("§4Usage: /audio syncarea <areaname> <seconds>");
                        }
                    }
                } else if (args[0].equalsIgnoreCase("defaultenable")) {
                    if (!sender.hasPermission("audioserver.defaultenable")) {
                        sender.sendMessage(unsufficient_permissions);
                    } else {
                        if (args.length >= 2) {
                            if (AudioAreas.size() > 0) {
                                for (AudioArea area : AudioAreas) {
                                    if (area.getAreaName().equalsIgnoreCase(args[1])) {
                                        area.setDefaultState(true);
                                        sender.sendMessage("§2Area state enabled!");
                                        return true;
                                    }
                                }
                                sender.sendMessage("§4The area [§c" + args[1] + "§4] was not found!");
                            } else {
                                sender.sendMessage("§4There are no audio areas yet!");
                            }
                        } else {
                            sender.sendMessage("§4Usage: /audio defaultenable <areaname>");
                        }
                    }
                } else if (args[0].equalsIgnoreCase("defaultdisable")) {
                    if (!sender.hasPermission("audioserver.defaultdisable")) {
                        sender.sendMessage(unsufficient_permissions);
                    } else {
                        if (args.length >= 2) {
                            if (AudioAreas.size() > 0) {
                                for (AudioArea area : AudioAreas) {
                                    if (area.getAreaName()
                                            .equalsIgnoreCase(args[1])) {
                                        area.setDefaultState(false);
                                        sender.sendMessage("§2Area state disabled!");
                                        return true;
                                    }
                                }
                                sender.sendMessage("§4The area [" + args[1]
                                        + "] was not found!");
                            } else {
                                sender.sendMessage("§4There are no audio areas yet!");
                            }
                        } else {
                            sender.sendMessage("§4Usage: /audio defaultdisable <areaname>");
                        }
                    }
                } else if (args[0].equalsIgnoreCase("triggerarea")) {
                    if (!sender.hasPermission("audioserver.triggerarea")) {
                        sender.sendMessage(unsufficient_permissions);
                    } else {
                        if (args.length >= 3) {
                            if (Bukkit.getPlayer(args[2]) != null) {
                                if (AudioAreas.size() > 0) {
                                    for (AudioArea area : AudioAreas) {
                                        if (area.getAreaName()
                                                .equalsIgnoreCase(args[1])) {
                                            area.triggerPlayer(Bukkit
                                                    .getPlayer(args[2]));
                                            return true;
                                        }
                                    }
                                    sender.sendMessage("§4The area ["
                                            + args[1] + "] was not found!");
                                } else {
                                    sender.sendMessage("§4There are no audio areas yet!");
                                }
                            } else {
                                sender.sendMessage("§4Couldn't find that player!");
                            }
                        } else {
                            sender.sendMessage("§4Usage: /audio triggerarea <areaname> <player>");
                        }
                    }
                } else if (args[0].equalsIgnoreCase("play")) {
                    if (!sender.hasPermission("audioserver.play")) {
                        sender.sendMessage(unsufficient_permissions);
                    } else {
                        if (args.length >= 4) {
                            if (sender instanceof BlockCommandSender || sender instanceof Player) {
                                Location loc = (sender instanceof Player) ? ((Player) sender).getLocation() :
                                        ((BlockCommandSender) sender).getBlock().getLocation().add(0.5, 0, 0.5);
                                String name = args[1];
                                double volume = AudioServer.get().getFloatFromString(
                                        args[2], 5);
                                double radius = AudioServer.get().getFloatFromString(
                                        args[3], 5);

                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    if (player.getLocation().distanceSquared(loc) < radius * radius) {

                                        PacketHelper.sendToPlayer(new PacketGlobalPlayOnce(
                                                ProtocolConfig.getAudioID(),
                                                name,
                                                (float) volume), player);
                                    }
                                }
                            } else {
                                sender.sendMessage("§4This command must be executed by either a player or commandblock");
                            }
                        } else
                            sender.sendMessage("§4Usage: /audio play <audioname> <volume> <radius>");
                    }
                } else if (args[0].equalsIgnoreCase("list")) {
                    if (!sender.hasPermission("audioserver.list")) {
                        sender.sendMessage(unsufficient_permissions);
                    } else {
                        if (AudioAreas.size() > 0) {
                            sender.sendMessage("§3[Audio areas]");
                            for (AudioArea area : AudioAreas) {
                                sender.sendMessage(((area.isEnabled()) ? "§a"
                                        : "§c")
                                        + area.getAreaName()
                                        + " §7- §b"
                                        + area.getSoundName()
                                        + ((area.getRepeat()) ? " §a► "
                                        : " §c► ")
                                        + "§aType: §b"
                                        + AudioArea.getType(area
                                        .getVehicleType()));
                            }
                        } else {
                            sender.sendMessage("§4There are no audio areas yet!");
                        }
                    }
                } else if (args[0].equalsIgnoreCase("setoptions")) {
                    if (!sender.hasPermission("audioserver.setoptions")) {
                        sender.sendMessage(unsufficient_permissions);
                    } else {
                        if (args.length >= 3) {
                            String name = args[1];
                            for (AudioArea area : AudioAreas) {
                                if (area.getAreaName().equalsIgnoreCase(
                                        name)) {
                                    for (int i = 2; i < args.length; i++) {
                                        sender.sendMessage(area
                                                .setOption(args[i]));
                                    }
                                    return true;
                                }
                            }
                            sender.sendMessage("§4Area §3" + name
                                    + "§4 wasn't found");
                        } else {
                            sender.sendMessage("§4Usage: /audio setoptions <areaname> [option:value] [...]");
                        }
                    }
                } else if (args[0].equalsIgnoreCase("getoptions")) {
                    if (!sender.hasPermission("audioserver.getoptions")) {
                        sender.sendMessage(unsufficient_permissions);
                    } else {
                        if (args.length >= 2) {
                            String name = args[1];
                            for (AudioArea area : AudioAreas) {
                                if (area.getAreaName().equalsIgnoreCase(name)) {
                                    area.sendOptions(sender);
                                    return true;
                                }
                            }
                            sender.sendMessage("§4Area §3" + name
                                    + "§4 wasn't found");
                        } else {
                            sender.sendMessage("§4Usage: /audio getoptions <areaname>");
                        }
                    }
                } else if (args[0].equalsIgnoreCase("remove")) {
                    if (!sender.hasPermission("audioserver.remove")) {
                        sender.sendMessage(unsufficient_permissions);
                    } else {
                        if (args.length >= 2) {
                            String name = args[1];
                            for (AudioArea area : AudioAreas) {
                                if (area.getAreaName().equalsIgnoreCase(name)) {
                                    synchronized (AudioAreas) {
                                        AudioServer.get().getAreaConfigFile()
                                                .set("areas." + area.getAreaName(), null);
                                        AudioAreas.remove(area);
                                        sender.sendMessage("§aArea " + name + " succesfully removed!");
                                    }
                                    return true;
                                }
                            }
                            sender.sendMessage("§4Area §3" + name + "§4 wasn't found");
                        } else {
                            sender.sendMessage("§4Usage: /audio remove <areaname>");
                        }
                    }
                } else if (args[0].equalsIgnoreCase("setselection")) {
                    if (!(sender instanceof Player)) {
                        sender.sendMessage("§4This command must be executed by a player");
                    } else if (!sender.hasPermission("audioserver.setselection")) {
                        sender.sendMessage(unsufficient_permissions);
                    } else {
                        if (args.length >= 2) {
                            String name = args[1];
                            for (AudioArea area : AudioAreas) {
                                if (area.getAreaName().equalsIgnoreCase(name)) {
                                    Player player = (Player) sender;
                                    WorldEditPlugin worldEditPlugin = (WorldEditPlugin) AudioServer.get().getServer().getPluginManager().getPlugin("WorldEdit");
                                    worldEditPlugin.setSelection(player, new CuboidSelection(area.getLocMax().getWorld(), area.getLocMax(), area.getLocMin()));
                                    sender.sendMessage("§2[Audio] The area of audio area " + area.getAreaName() + " has been set as your WE-selection!");
                                    return true;
                                }
                            }
                            sender.sendMessage("§4Area §3" + name + "§4 wasn't found");
                        } else {
                            sender.sendMessage("§4Usage: /audio setselection <areaname>");
                        }
                    }
                } else if (args[0].equalsIgnoreCase("redefinearea")) {
                    if (!(sender instanceof Player)) {
                        sender.sendMessage("§4This command must be executed by a player");
                    } else if (!sender.hasPermission("audioserver.redefinearea")) {
                        sender.sendMessage(unsufficient_permissions);
                    } else {
                        Player player = (Player) sender;
                        WorldEditPlugin worldEditPlugin = (WorldEditPlugin) AudioServer.get().getServer().getPluginManager().getPlugin("WorldEdit");
                        Selection sel = worldEditPlugin.getSelection(player);
                        if (sel instanceof CuboidSelection) {
                            if (args.length >= 2) {
                                String name = args[1];
                                for (AudioArea area : AudioAreas) {
                                    if (area.getAreaName().equalsIgnoreCase(name)) {
                                        area.setArea(sel.getMinimumPoint(), sel.getMaximumPoint());
                                        sender.sendMessage("§2[Audio] Audio area redefined!");
                                        return true;
                                    }
                                }
                                sender.sendMessage("§4Area §3" + name + "§4 wasn't found");
                            } else {
                                sender.sendMessage("§4Usage: /audio redefinearea <areaname>");
                            }
                        } else {
                            sender.sendMessage("§4Select an area using WorldEdit wand first!");
                        }
                    }
                } else if (args[0].equalsIgnoreCase("add")) {
                    if (!(sender instanceof Player)) {
                        sender.sendMessage("§4This command must be executed by a player");
                    } else if (!sender.hasPermission("audioserver.add")) {
                        sender.sendMessage(unsufficient_permissions);
                    } else {
                        Player player = (Player) sender;
                        WorldEditPlugin worldEditPlugin = (WorldEditPlugin) AudioServer.get().getServer().getPluginManager().getPlugin("WorldEdit");
                        Selection sel = worldEditPlugin.getSelection(player);
                        if (sel instanceof CuboidSelection) {
                            if (args.length >= 3) {
                                String name = args[1];
                                for (AudioArea area : AudioAreas) {
                                    if (area.getAreaName().equalsIgnoreCase(name)) {
                                        sender.sendMessage("§4The area " + name + " already exists. Choose another name.");
                                        return true;
                                    }
                                }
                                String soundname = args[2];
                                double volume = 1;
                                int fadetime = 500;
                                if (args.length >= 4) {
                                    try {
                                        volume = Double.parseDouble(args[3]);
                                    } catch (Exception e) {
                                        sender.sendMessage("§4" + args[3] + " is not a floating point!");
                                        return true;
                                    }
                                }

                                if (args.length >= 5) {
                                    try {
                                        fadetime = Integer.parseInt(args[4]);
                                    } catch (Exception e) {
                                        sender.sendMessage("§4" + args[3] + " is not a floating point!");
                                        return true;
                                    }
                                }

                                AudioArea NewArea = new AudioArea(AudioServer.get(),
                                        name, soundname, fadetime, volume,
                                        "loop", sel.getMinimumPoint(),
                                        sel.getMaximumPoint(), true, true,
                                        AudioArea.VEHICLE_DEFAULT, true);
                                NewArea.saveToConfig(AudioServer.get().getAreaConfigFile());
                                AudioAreas.add(NewArea);

                                sender.sendMessage("§aArea succesfully added! " + name + " - " + soundname);
                            } else {
                                sender.sendMessage("§4Usage: /audio add <name> <soundname> [volume] [fadetime]");
                            }
                        } else {
                            sender.sendMessage("§4Select an area using WorldEdit wand first!");
                        }
                    }
                } else if (args[0].equalsIgnoreCase("save")) {
                    if (!sender.hasPermission("audioserver.save")) {
                        sender.sendMessage(unsufficient_permissions);
                    } else {
                        AudioServer.get().saveAreas();
                    }
                } else {
                    sender.sendMessage("§eI don't know what you're doing?");
                }
                return true;
            }
            return true;
        }
        return false;
    }
}
