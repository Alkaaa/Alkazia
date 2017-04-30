package fr.thisismac.koth;

import java.util.Map.Entry;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTask extends BukkitRunnable{
	
	private Core core;
	private int time;
	private int current;
	public int id;
	
	public GameTask(Core c, int delay) {
		this.core = c;
		this.time = delay;
		this.current = delay;
	}

	@Override
	public void run() {
		if(current == 0) {
			this.stopGame();
		}
		for(Entry<String, Integer> entry : core.players.entrySet()) {
			entry.setValue(entry.getValue() + 1);
			sendLoad(entry.getKey(), entry.getValue());
			
			if(entry.getValue() >= 300) {
				stopGame(entry.getKey());
			}
		}
		
		current--;
	}
	
	public void stopGame(String winner) {
		for(Entry<String, Integer> player : core.players.entrySet()) {
			Bukkit.getPlayer(player.getKey()).sendMessage("[BOSSBAR]REMOVE");
		}
		
		TitleManager.sendTitleToAllPlayers(core.prefix, ChatColor.GREEN + winner + " a réussi à dominer le monument du KOTH pendant 3 minutes.");
		core.running = false;
		core.getServer().getScheduler().cancelTask(id);
		core.awardPlayer(winner);
	}
	
	public void stopGame() {
		for(Entry<String, Integer> player : core.players.entrySet()) {
			Bukkit.getPlayer(player.getKey()).sendMessage("[BOSSBAR]REMOVE");
		}
		
		TitleManager.sendTitleToAllPlayers(core.prefix, ChatColor.RED + "Personne n'a reussi à dominer le monument du KOTH.");
		core.running = false;
		core.getServer().getScheduler().cancelTask(id);
	}

	public void sendLoad(String s, int i) {
		Bukkit.getPlayer(s).sendMessage("[BOSSBAR]:" + String.valueOf((float)i / 300) + ":" +  core.prefix + ChatColor.GREEN + "Capture du KOTH en cours ...");
	}

	 
}
