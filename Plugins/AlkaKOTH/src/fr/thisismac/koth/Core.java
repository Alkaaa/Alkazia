package fr.thisismac.koth;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.server.v1_8_R1.ChatSerializer;
import net.minecraft.server.v1_8_R1.IChatBaseComponent;
import net.minecraft.server.v1_8_R1.PacketPlayOutChat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.Kit;

public class Core extends JavaPlugin {
	
    public File 						 configFile;
    public FileConfiguration			 config;
    public Cuboid 						 cubo;
	public String 						 prefix 			= ChatColor.DARK_RED + "[" + ChatColor.GOLD + "AlkaKOTH" + ChatColor.DARK_RED + "] " + ChatColor.RESET;
	public GameTask						 gameTask;
	public List<String>					 reward;
	public Essentials					 api;
	public HashMap<String, String>		 rewards 			= new HashMap<String, String>();
	public HashMap<String, Integer>		 players		    = new HashMap<String, Integer>();
	public boolean 						 enabled;
	public boolean 						 running			= false;
	public static Core 					 plugin;
   

    @Override
    public void onDisable() {
        this.saveConfig();
        if(isRunning()) {
        	gameTask.stopGame();
        }
    }

	@Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new Listeners(this), this);
        this.loadConfig();
        this.loadData();
        this.plugin = this;
        
        this.timeTask();
        this.api = (Essentials) getServer().getPluginManager().getPlugin("Essentials");
        this.getCommand("alkakoth").setExecutor(new CmdExecutor(this));
        
    }
    
    // FONCTIONS
    
    public void awardPlayer(String playerName) {
        final Player p = Bukkit.getPlayer(playerName);
        for (String kitName : reward) {
        	try {
				if(getFreeSlot(p.getInventory()) >=  api.getSettings().getKit(kitName.toLowerCase()).size()) {
				  Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kit " + kitName + " " + p.getName());
				}
				else {
					p.sendMessage(prefix + ChatColor.GREEN + "Vous n'avez pas de place pour recevoir une récompense, vous la recevrez à votre prochaine connexion si vous avez de la place sur vous.");
					rewards.put(playerName, kitName);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
       
    }

    @SuppressWarnings("unchecked")
	public void loadData() {
        final World world = this.getServer().getWorld(this.config.getString("cuboid.world"));
        this.reward = this.config.getStringList("reward");
        
        this.cubo = new Cuboid(new Location(world, config.getDouble("cuboid.min.x"), config.getDouble("cuboid.min.y"), config.getDouble("cuboid.min.z")),
        		new Location(world, config.getDouble("cuboid.max.x"), config.getDouble("cuboid.max.y"), config.getDouble("cuboid.max.z")));
        
        this.enabled = config.getBoolean("enable");
        
    }
   
    public void saveData() {
    	
        this.config.set("cuboid.min.x", this.cubo.getLowerX());
        this.config.set("cuboid.min.y", this.cubo.getLowerY());
        this.config.set("cuboid.min.z", this.cubo.getLowerZ());
        this.config.set("cuboid.max.x", this.cubo.getUpperX());
        this.config.set("cuboid.max.y", this.cubo.getUpperY());
        this.config.set("cuboid.max.z", this.cubo.getUpperZ());
        this.config.set("cuboid.world", this.cubo.getWorld());
        this.config.set("enable", enabled);
        
        try {
            this.config.save(this.configFile);
        } catch (final IOException e) {
            this.getLogger().warning("Cannot save config.yml at starting!");
            e.printStackTrace();
        }
    }

    public void loadConfig() {
        if (!this.getDataFolder().exists()) this.getDataFolder().mkdir();
        this.configFile = new File(this.getDataFolder(), "config.yml");
        this.config = YamlConfiguration.loadConfiguration(this.configFile);
        
	        if (!this.configFile.exists()) {
	        	try {
		            final String[] tableau = { "alchimiste", "meteorP4"};
		            final List<String> tabList = Arrays.asList(tableau);
		            this.config.set("cuboid.min.x", 0);
		            this.config.set("cuboid.min.y", 0);
		            this.config.set("cuboid.min.z", 0);
		            this.config.set("cuboid.max.x", 0);
		            this.config.set("cuboid.max.y", 0);
		            this.config.set("cuboid.max.z", 0);
		            this.config.set("cuboid.world", "world");
		            this.config.set("reward", tabList);
		            this.config.set("cuboid.world", "world");
		            this.config.set("enable", false);
		            this.config.save(this.configFile);
		            loadConfig();
	            
	        } catch (final IOException e) {
	            this.getLogger().warning("Cannot save config.yml at starting!");
	            e.printStackTrace();
	        }
        }
    }
    
    public int getFreeSlot(Inventory inv) {
    	int freeslots = 0;
    	for(int x = 0; x < inv.getSize(); x++) {
    		if(inv.getItem(x) == null) {
    			freeslots++;
    		}
    	}
    	return freeslots;
    }
    
    public void startGame() {
		this.running = true;
		this.gameTask = new GameTask(this, 3600);
		this.gameTask.id = getServer().getScheduler().scheduleSyncRepeatingTask(this, gameTask, 0, 20);
		TitleManager.sendTitleToAllPlayers(prefix, ChatColor.GREEN + "Les passages vers le KOTH sont ouverts, il est disponible à la capture.");
	}


    public void timeTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                final Calendar calendar = Calendar.getInstance();
                if(calendar.get(Calendar.MINUTE) == 55 && (calendar.get(Calendar.HOUR_OF_DAY) == 17 || calendar.get(Calendar.HOUR_OF_DAY) == 20)) {
                	if(enabled) {
                    	TitleManager.sendTitleToAllPlayers(prefix, ChatColor.GREEN + "Les passages des temples vers le KOTH vont s'ouvrir dans 10 minutes ..");
                	}
                	else {
                		getLogger().info("L'activation automatique a été désactivé, le broadcast du koth n'a donc pas démarré.");
                	}
                }
                if(calendar.get(Calendar.MINUTE) == 50 && (calendar.get(Calendar.HOUR_OF_DAY) == 18 || calendar.get(Calendar.HOUR_OF_DAY) == 21)) {
                	if(enabled) {
                    	TitleManager.sendTitleToAllPlayers(prefix, ChatColor.RED + "Les passages des temples vers le KOTH vont se fermer dans 10 minutes ..");
                	}
                	else {
                		getLogger().info("L'activation automatique a été désactivé, le broadcast du koth ne s'est pas arrété.");
                	}
                }
                if(calendar.get(Calendar.MINUTE) == 0 && (calendar.get(Calendar.HOUR_OF_DAY) == 18) || (calendar.get(Calendar.HOUR_OF_DAY) == 21)) {
                	if(enabled) {
                    	startGame();
                	}
                	else {
                		getLogger().info("L'activation automatique a été désactivé, le koth n'a donc pas démarré.");
                	}
                }
            }
        }.runTaskTimer(this, 0, 60 * 20);
    }
    
    
   public void sendActionBar(Player p, String msg) {
	   	IChatBaseComponent cbc = ChatSerializer.a("{\"text\": \"" + msg + "\"}");
		PacketPlayOutChat chat = new PacketPlayOutChat(cbc, (byte)2);
		((CraftPlayer)p).getHandle().playerConnection.sendPacket(chat);
   }
   


	public boolean isRunning() {
		return running;
	}
	
	public static Core getInstance() {
		return plugin;
	}
}
