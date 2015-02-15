package net.craftventure.audioserver;

import net.craftventure.audioserver.commands.CommandAudio;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AudioServer extends JavaPlugin {
    public static final String AUDIOSERVER_PLUGIN_CHANNEL = "AudioServer";
    private static AudioServer _as = null;

    private final List<AudioArea> AudioAreas = new ArrayList<AudioArea>();

    private File areasFile;
    private FileConfiguration areasConfig;

    public static AudioServer get() {
        return _as;
    }

    public List<AudioArea> getAudioAreas() {
        return AudioAreas;
    }

    public File getAreaFile() {
        return areasFile;
    }

    public FileConfiguration getAreaConfigFile() {
        return areasConfig;
    }

    @Override
    public void onEnable() {
        _as = this;
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, AUDIOSERVER_PLUGIN_CHANNEL);
        this.saveDefaultConfig();
        new GeneralListener(this);

        parseAreas();

        getCommand("setaudio").setExecutor(new CommandAudio());
        getCommand("audio").setExecutor(new CommandAudio());
        getServer().broadcast("§2AudioServer plugin enabled with " + this.getAudioAreas().size() + " areas!", "parkprotect.admin");
    }

    @Override
    public void onDisable() {
        saveAreas();
        getServer().broadcast("§4AudioServer plugin disabled!", "parkprotect.admin");
    }

    public void parseAreas() {
        try {
            firstRun();


            Set<String> areaNames = areasConfig.getConfigurationSection("areas").getKeys(false);
            for(String areaname : areaNames) {
                try {
                    ConfigurationSection CurrentArea = areasConfig.getConfigurationSection("areas." + areaname);

                    World world = Bukkit.getWorld(CurrentArea.getString("world"));

                    Location corner_1 = new Location(world,
                            CurrentArea.getConfigurationSection("corner_1").getInt("x"),
                            CurrentArea.getConfigurationSection("corner_1").getInt("y"),
                            CurrentArea.getConfigurationSection("corner_1").getInt("z"));

                    Location corner_2 = new Location(world,
                            CurrentArea.getConfigurationSection("corner_2").getInt("x"),
                            CurrentArea.getConfigurationSection("corner_2").getInt("y"),
                            CurrentArea.getConfigurationSection("corner_2").getInt("z"));

                    AudioArea area = new AudioArea(this, areaname,
                            CurrentArea.getString("soundname"),
                            CurrentArea.getInt("fadetime"),
                            CurrentArea.getDouble("volume"),
                            CurrentArea.getString("type"), corner_1, corner_2,
                            CurrentArea.getBoolean("enabled"),
                            CurrentArea.getBoolean("startonenter"),
                            CurrentArea.getInt("vehicletype"),
                            CurrentArea.getBoolean("repeat"));

                    if (world == null) {
                        area.setEnabled(false);
                    }

                    AudioAreas.add(area);
                } catch (Exception e) {
                    getLogger().warning("Failed to load area " + areaname + ": " + e.getMessage());
                }
            }

            getLogger().info("AudioServer loaded a total of " + AudioAreas.size() + " Audio Areas");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveAreas() {
        for (AudioArea area : AudioAreas) {
            area.saveToConfig(areasConfig);
        }
    }

    public void loadYamls() {
        try {
            areasConfig.load(areasFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void firstRun() throws Exception {
        if(areasFile == null) {
            areasFile = new File(getDataFolder(), "areas.yml");
        }
        if (!areasFile.exists()) {
            areasFile.getParentFile().mkdirs();
            copy(getResource("areas.yml"), areasFile);
        }

        if(areasConfig == null) {
            areasConfig = new YamlConfiguration();
        }
        loadYamls();
    }

    private void copy(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public int getIntFromString(String str, int defval) {
        int ret = defval;
        if (!isEmpty(str))
            try {
                ret = Integer.parseInt(str);
            } catch (NumberFormatException ne) {
                ret = defval;
            }
        return ret;
    }

    public float getFloatFromString(String str, float defval) {
        float ret = defval;
        if (!isEmpty(str))
            try {
                ret = Float.parseFloat(str);
            } catch (NumberFormatException ne) {
                ret = defval;
            }
        return ret;
    }

    public double getDoubleFromString(String str, double defval) {
        double ret = defval;
        if (!isEmpty(str))
            try {
                ret = Double.parseDouble(str);
            } catch (NumberFormatException ne) {
                ret = defval;
            }
        return ret;
    }

    public AudioArea getByName(String name) {
        return null;
    }
}
