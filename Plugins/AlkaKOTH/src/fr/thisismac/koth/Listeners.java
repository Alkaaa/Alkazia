package fr.thisismac.koth;

import java.util.Iterator;
import java.util.Map.Entry;

import net.ess3.api.InvalidWorldException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffectType;

import com.earth2me.essentials.Kit;
import com.earth2me.essentials.commands.WarpNotFoundException;

public class Listeners implements Listener {
    private final Core core;

    public Listeners(Core c) {
        this.core = c;
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
    	final Player p = e.getPlayer();
        if(core.rewards.containsKey(e.getPlayer().getName())) {
        	Iterator<?> it = (Iterator<?>)core.rewards.entrySet().iterator();
        	while(it.hasNext()) {
        		@SuppressWarnings("unchecked")
				Entry<String, String> entry = (Entry<String, String>)it.next();
        		if(e.getPlayer().getName().equalsIgnoreCase(entry.getValue())) {
        			try {
						Kit k = new Kit(entry.getValue(), core.api);
						if(core.getFreeSlot(p.getInventory()) >=  k.getItems(core.api.getUser(p.getName())).size()) {
							  Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kit " + entry.getValue() + " " + p.getName());
							  core.rewards.remove(p.getName());
							  p.sendMessage(core.prefix + ChatColor.GREEN + "Vous avez recu les récompenses du KOTH que vous aviez gagné.");
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
        		}
        	}
        }
    }
    
    @EventHandler
    public void onMove(final PlayerMoveEvent e) {
    	if(core.isRunning()) {
    		if(core.cubo.contains(e.getTo())) {
    			if(e.getPlayer().getActivePotionEffects().contains(PotionEffectType.INVISIBILITY)) {
    				e.getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
    				e.getPlayer().sendMessage(core.prefix + ChatColor.RED + "Vous ne pouvez pas participé au KOTH en étant invisible.");
    			}
    			if(!core.players.containsKey(e.getPlayer().getName())) {
    				core.players.put(e.getPlayer().getName(), 0);
    				TitleManager.sendTitle(e.getPlayer(), core.prefix, ChatColor.GREEN + "Vous devez resté sur le monument pendant 3 minutes (no-stop) !");
    			}
    		}
    		else if(core.cubo.contains(e.getFrom()) && !core.cubo.contains(e.getTo())) {
    			core.players.remove(e.getPlayer().getName());
        		e.getPlayer().sendMessage("[BOSSBAR]REMOVE");
				TitleManager.sendTitle(e.getPlayer(), core.prefix, ChatColor.RED + "Vous avez quitté le monument.");
    		}
    		else if(!core.cubo.contains(e.getPlayer().getLocation()) && core.players.containsKey(e.getPlayer().getName())) {
    			core.players.remove(e.getPlayer().getName());
        		e.getPlayer().sendMessage("[BOSSBAR]REMOVE");
				TitleManager.sendTitle(e.getPlayer(), core.prefix, ChatColor.RED + "Vous avez quitté le monument.");
    		}
    	}
    }
    
    @EventHandler
    public void onQuit(final PlayerQuitEvent e) {
    	if(core.cubo.contains(e.getPlayer().getLocation())) {
    		e.getPlayer().teleport(core.getServer().getWorld("world").getSpawnLocation());
    	}
    	if(core.isRunning() && core.players.containsKey(e.getPlayer().getName())) {
    		core.players.remove(e.getPlayer().getName());
    		e.getPlayer().sendMessage("[BOSSBAR]REMOVE");
    	}
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled=true)
    public void onInteract(final PlayerInteractAtEntityEvent e) {
    	if(e.getRightClicked().getType() == EntityType.ARMOR_STAND) {
        	String custom = e.getRightClicked().getCustomName();
        		if(custom != null && custom.contains("Gardien du KOTH")) {
        			try {
	        			if(custom.contains("1")) {
	        				if(core.isRunning()) e.getPlayer().teleport(core.api.getWarps().getWarp("koth_spawn1"));
	        				else e.setCancelled(true);
	        			}
	        			else if(custom.contains("2")) {
	        				if(core.isRunning()) e.getPlayer().teleport(core.api.getWarps().getWarp("koth_spawn2"));
	        				else e.setCancelled(true);
	        			}
	        			else if(custom.contains("3")) {
	        				if(core.isRunning()) e.getPlayer().teleport(core.api.getWarps().getWarp("koth_spawn3"));
	        				else e.setCancelled(true);
	        			}
	        			else if(custom.contains("4")) {
	        				if(core.isRunning()) e.getPlayer().teleport(core.api.getWarps().getWarp("koth_spawn4"));
	        				else e.setCancelled(true);
	        			}
        			} catch (WarpNotFoundException | InvalidWorldException e1) {
							e1.printStackTrace();
						}
        			e.setCancelled(true);
        		}
    	}
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled=true)
    public void ArmorStandDestroy(EntityDamageByEntityEvent e){
       if(!(e.getEntity().getType() == EntityType.ARMOR_STAND)) return;
       
       ArmorStand armor = (ArmorStand)e.getEntity();
       if(armor.getCustomName() != null && armor.getCustomName().contains("Gardien du KOTH")) {
    	   e.setCancelled(true);
       }
    }
    

}
